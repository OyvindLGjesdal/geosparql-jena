/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index.expiring;

import static implementation.index.IndexDefaultValues.FULL_INDEX_WARNING_INTERVAL;
import static implementation.index.IndexDefaultValues.INDEX_CLEANER_INTERVAL;
import static implementation.index.IndexDefaultValues.INDEX_EXPIRY_INTERVAL;
import static implementation.index.IndexDefaultValues.MINIMUM_INDEX_CLEANER_INTERVAL;
import static implementation.index.IndexDefaultValues.UNLIMITED_INDEX;
import static implementation.index.IndexDefaultValues.UNLIMITED_INITIAL_CAPACITY;
import java.lang.invoke.MethodHandles;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @param <K>
 * @param <V>
 */
public class ExpiringIndex<K, V> extends ConcurrentHashMap<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private int maxSize;
    private long expiryInterval;
    private long fullIndexWarningInterval;
    private long fullIndexWarning;
    private String label;
    private final ExpiringIndexCleaner indexCleaner;
    private Timer cleanerTimer;
    private long cleanerInterval = INDEX_CLEANER_INTERVAL;

    public ExpiringIndex(int maxSize, String label) {
        this(maxSize, INDEX_EXPIRY_INTERVAL, FULL_INDEX_WARNING_INTERVAL, label);
    }

    public ExpiringIndex(int maxSize, long expiryInterval, String label) {
        this(maxSize, expiryInterval, FULL_INDEX_WARNING_INTERVAL, label);
    }

    public ExpiringIndex(int maxSize, long expiryInterval, long fullIndexWarningInterval, String label) {
        super(maxSize > UNLIMITED_INDEX ? maxSize : UNLIMITED_INITIAL_CAPACITY);
        this.maxSize = maxSize > UNLIMITED_INDEX ? maxSize : Integer.MAX_VALUE;
        this.expiryInterval = expiryInterval;
        this.fullIndexWarningInterval = fullIndexWarningInterval;
        this.fullIndexWarning = System.currentTimeMillis();
        this.label = label;
        this.indexCleaner = new ExpiringIndexCleaner(this, expiryInterval);
        this.cleanerTimer = null;
    }

    @Override
    public V put(K key, V value) {
        if (super.size() < maxSize) {
            indexCleaner.update(key);
            return super.put(key, value);
        } else {
            long currentSystemTime = System.currentTimeMillis();
            if (currentSystemTime - fullIndexWarning > fullIndexWarningInterval) {
                fullIndexWarning = currentSystemTime;
                LOGGER.warn("{} Index Full: {} - Warning suppressed for {}ms", label, maxSize, fullIndexWarningInterval);
            }
        }

        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean isContained = super.containsKey(key);
        if (isContained) {
            indexCleaner.update(key);
        }
        return isContained;
    }

    @Override
    public V get(Object key) {
        V value = super.get(key);
        if (value != null) {
            indexCleaner.update(key);
        }
        return value;
    }

    @Override
    public void clear() {
        super.clear();
        indexCleaner.clear();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize > UNLIMITED_INDEX ? maxSize : Integer.MAX_VALUE;
    }

    public long getExpiryInterval() {
        return expiryInterval;
    }

    public void setExpiryInterval(long expiryInterval) {

        if (expiryInterval < cleanerInterval) {
            this.expiryInterval = cleanerInterval * 2;
            LOGGER.warn("Expiry Interval: {} cannot be less than Cleaner Interval: {}. Setting to twice the Cleaner Interval: {}", expiryInterval, cleanerInterval, expiryInterval);
        } else {
            this.expiryInterval = expiryInterval;
        }
        this.indexCleaner.setExpiryInterval(this.expiryInterval);
    }

    public long getFullIndexWarningInterval() {
        return fullIndexWarningInterval;
    }

    public void setFullIndexWarningInterval(long fullIndexWarningInterval) {
        this.fullIndexWarningInterval = fullIndexWarningInterval;
    }

    public long getFullIndexWarning() {
        return fullIndexWarning;
    }

    public void setFullIndexWarning(long fullIndexWarning) {
        this.fullIndexWarning = fullIndexWarning;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void startExpiry() {
        startExpiry(cleanerInterval);
    }

    public void startExpiry(long cleanerInterval) {
        if (cleanerTimer != null) {
            cleanerTimer.cancel();
        }

        if (MINIMUM_INDEX_CLEANER_INTERVAL < cleanerInterval) {
            this.cleanerInterval = cleanerInterval;
        } else {
            LOGGER.warn("Cleaner Interval: {} less than minimum: {}. Setting to minimum.", cleanerInterval, MINIMUM_INDEX_CLEANER_INTERVAL);
            this.cleanerInterval = MINIMUM_INDEX_CLEANER_INTERVAL;
        }
        cleanerTimer = new Timer(label, true);
        cleanerTimer.scheduleAtFixedRate(indexCleaner, this.cleanerInterval, this.cleanerInterval);
    }

    public void stopExpiry() {
        if (cleanerTimer != null) {
            cleanerTimer.cancel();
            cleanerTimer = null;
        }
    }

    @Override
    public String toString() {
        return "ExpiringIndex{" + "maxSize=" + maxSize + ", expiryInterval=" + expiryInterval + ", fullIndexWarningInterval=" + fullIndexWarningInterval + ", fullIndexWarning=" + fullIndexWarning + ", label=" + label + ", indexCleaner=" + indexCleaner + ", cleanerTimer=" + cleanerTimer + '}';
    }

}
