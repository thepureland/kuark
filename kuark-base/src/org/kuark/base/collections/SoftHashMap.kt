package org.kuark.base.collections

import java.lang.ref.ReferenceQueue
import java.lang.ref.SoftReference
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.locks.ReentrantLock

/**
 * A `*Soft*HashMap` is a memory-constrained map that stores
 * its *values* in [SoftReference]s. (Contrast this
 * with the JDK's [SoftHashMap], which uses weak references
 * for its *keys*, which is of little value if you want the cache to
 * auto-resize itself based on memory constraints).
 *
 *
 * Having the values wrapped by soft references allows the cache to
 * automatically reduce its size based on memory limitations and garbage
 * collection. This ensures that the cache will not cause memory leaks by
 * holding strong references to all of its values.
 *
 *
 * This class is a generics-enabled Map based on initial ideas from Heinz
 * Kabutz's and Sydney Redelinghuys's [publicly posted
 * version (with their approval)](http://www.javaspecialists.eu/archive/Issue015.html), with continued modifications.
 *
 *
 * This implementation is thread-safe and usable in concurrent environments.
 *
 * @since 1.0.0
 */
internal class SoftHashMap<K, V> constructor(retentionSize: Int = DEFAULT_RETENTION_SIZE) : MutableMap<K, V> {

    /**
     * The internal HashMap that will hold the SoftReference.
     */
    private val map: MutableMap<K, SoftValue<V, K>?>

    /**
     * The number of strong references to hold internally, that is, the number
     * of instances to prevent from being garbage collected automatically
     * (unlike other soft references).
     */
    private val RETENTION_SIZE: Int

    /**
     * The FIFO list of strong references (not to be garbage collected), sort
     * of last access.
     */
    private val strongReferences // guarded by
            : Queue<V>

    // 'strongReferencesLock'
    private val strongReferencesLock: ReentrantLock

    /**
     * Reference queue for cleared SoftReference objects.
     */
    private val queue: ReferenceQueue<in V>

    /**
     * Creates a `SoftHashMap` backed by the specified `source`,
     * with a default retention size of [ DEFAULT_RETENTION_SIZE][.DEFAULT_RETENTION_SIZE] (100 entries).
     *
     * @param source the backing map to populate this `SoftHashMap`
     * @see .SoftHashMap
     */
    constructor(source: Map<K, V>) : this(DEFAULT_RETENTION_SIZE) {
        putAll(source)
    }

    /**
     * Creates a `SoftHashMap` backed by the specified `source`,
     * with the specified retention size.
     *
     *
     * The retention size (n) is the total number of most recent entries in the
     * map that will be strongly referenced (ie 'retained') to prevent them from
     * being eagerly garbage collected. That is, the point of a SoftHashMap is
     * to allow the garbage collector to remove as many entries from this map as
     * it desires, but there will always be (n) elements retained after a GC due
     * to the strong references.
     *
     *
     * Note that in a highly concurrent environments the exact total number of
     * strong references may differ slightly than the actual
     * `retentionSize` value. This number is intended to be a
     * best-effort retention low water mark.
     *
     * @param source the backing map to populate this `SoftHashMap`
     * @param retentionSize the total number of most recent entries in the map
     * that will be strongly referenced (retained), preventing them
     * from being eagerly garbage collected by the JVM.
     */
    constructor(source: Map<K, V>, retentionSize: Int) : this(retentionSize) {
        putAll(source)
    }

    override operator fun get(key: K): V? {
        processQueue()
        var result: V? = null
        val value = map.get(key)
        if (value != null) {
            // unwrap the 'real' value from the SoftReference
            result = value.get()
            result?.let {
                // Add this value to the beginning of the strong reference queue
                // (FIFO).
                addToStrongReferences(it)
            } ?: // The wrapped value was garbage collected, so remove this entry
            // from the backing map:
            // noinspection SuspiciousMethodCalls
            map.remove(key)
        }
        return result
    }

    private fun addToStrongReferences(result: V) {
        strongReferencesLock.lock()
        try {
            strongReferences.add(result)
            trimStrongReferencesIfNecessary()
        } finally {
            strongReferencesLock.unlock()
        }
    }

    // Guarded by the strongReferencesLock in the addToStrongReferences method
    private fun trimStrongReferencesIfNecessary() {
        // trim the strong ref queue if necessary:
        while (strongReferences.size > RETENTION_SIZE) {
            strongReferences.poll()
        }
    }

    /**
     * Traverses the ReferenceQueue and removes garbage-collected SoftValue
     * objects from the backing map by looking them up using the SoftValue.key
     * data member.
     */
    private fun processQueue() {
        lateinit var sv: SoftValue<V, K>
        while ((queue.poll() as SoftValue<V, K>)?.also { sv = it } != null) {
            // noinspection SuspiciousMethodCalls
            map.remove(sv.key) // we can access private data!
        }
    }

    override fun isEmpty(): Boolean {
        processQueue()
        return map.isEmpty()
    }

    override fun containsKey(key: K): Boolean {
        processQueue()
        return map.containsKey(key)
    }

    override fun containsValue(value: V): Boolean {
        processQueue()
        val values: Collection<*> = values
        return values.contains(value)
    }

    override fun putAll(m: Map<out K, V>) {
        if (m == null || m.isEmpty()) {
            processQueue()
            return
        }
        for ((key, value) in m) {
            put(key, value)
        }
    }

    override val keys
        get() = run {
            processQueue()
            map.keys
        }


    override val values
        get() = run {
            processQueue()
            val keys: Collection<K> = map.keys
            val values = ArrayList<V>(keys.size)
            for (key in keys) {
                val v = get(key)
                if (v != null) {
                    values.add(v)
                }
            }
            values
        }

    /**
     * Creates a new entry, but wraps the value in a SoftValue instance to
     * enable auto garbage collection.
     */
    override fun put(key: K, value: V): V? {
        processQueue() // throw out garbage collected values first
        val sv =
            SoftValue(value, key, queue)
        val previous = map.put(key, sv)
        addToStrongReferences(value)
        return previous?.get()
    }

    override fun remove(key: K): V? {
        processQueue() // throw out garbage collected values first
        val raw = map.remove(key)
        return raw?.get()
    }

    override fun clear() {
        strongReferencesLock.lock()
        try {
            strongReferences.clear()
        } finally {
            strongReferencesLock.unlock()
        }
        processQueue() // throw out garbage collected values
        map.clear()
    }

    override val size
        get() = run {
            processQueue()
            map.size
        }

    override val entries
        get() = run {
            processQueue() // throw out garbage collected values first
            val keys: Collection<K> = map.keys
            val kvPairs = HashMap<K, V>(keys.size)
            for (key in keys) {
                val v = get(key)
                if (v != null) {
                    kvPairs[key] = v
                }
            }
            kvPairs.entries
        }


    /**
     * We define our own subclass of SoftReference which contains not only the
     * value but also the key to make it easier to find the entry in the HashMap
     * after it's been garbage collected.
     */
    private class SoftValue<V : Any?, K : Any?>
    /**
     * Constructs a new instance, wrapping the value, key, and queue, as
     * required by the superclass.
     *
     * @param value the map value
     * @param key the map key
     * @param queue the soft reference queue to poll to determine if the
     * entry had been reaped by the GC.
     */(value: V, val key: K, queue: ReferenceQueue<in V>) :
        SoftReference<V>(value, queue)

    companion object {
        /**
         * The default value of the RETENTION_SIZE attribute, equal to 100.
         */
        private const val DEFAULT_RETENTION_SIZE = 100

        @Throws(InterruptedException::class)
        @JvmStatic
        fun main(args: Array<String>) {
//		int size = 10000000;
//		long s = System.currentTimeMillis();
////		HashMap<String, Integer> map = new HashMap<String, Integer>(size);
//		SoftHashMap<String, Integer> map = new SoftHashMap<String, Integer>(size);
//		
//		for (int i = 0; i < size; i++) {
//			map.put(i+"", i);
//		}
//		System.out.println(System.currentTimeMillis() - s);
//		
//		System.gc();
//		Thread.sleep(5000L);
//		System.out.println(map.get("1"));
            val maps: MutableList<WeakHashMap<Array<ByteArray>, Array<ByteArray>>> =
                ArrayList()
            for (i in 0..999) {
                val d =
                    WeakHashMap<Array<ByteArray>, Array<ByteArray>>()
                d[Array(500) { ByteArray(500) }] = Array(500) { ByteArray(500) }
                for (j in 0..99) {
                    maps.add(d)
                }
                System.gc()
                System.err.println(i)
                for (j in 0 until i) {
                    val size = maps[j].size
                    System.err.println("$j size$size")
                    //					if(size == 0) {
//						System.exit(1);
//					}
                }
            }
        }
    }
    /**
     * Creates a new SoftHashMap with the specified retention size.
     *
     *
     * The retention size (n) is the total number of most recent entries in the
     * map that will be strongly referenced (ie 'retained') to prevent them from
     * being eagerly garbage collected. That is, the point of a SoftHashMap is
     * to allow the garbage collector to remove as many entries from this map as
     * it desires, but there will always be (n) elements retained after a GC due
     * to the strong references.
     *
     *
     * Note that in a highly concurrent environments the exact total number of
     * strong references may differ slightly than the actual
     * `retentionSize` value. This number is intended to be a
     * best-effort retention low water mark.
     *
     * @param retentionSize the total number of most recent entries in the map
     * that will be strongly referenced (retained), preventing them
     * from being eagerly garbage collected by the JVM.
     */
    /**
     * Creates a new SoftHashMap with a default retention size size of
     * [DEFAULT_RETENTION_SIZE][.DEFAULT_RETENTION_SIZE] (100 entries).
     *
     * @see .SoftHashMap
     */
    init {
        RETENTION_SIZE = Math.max(0, retentionSize)
        queue = ReferenceQueue()
        strongReferencesLock = ReentrantLock()
        map = ConcurrentHashMap()
        strongReferences = ConcurrentLinkedQueue()
    }
}