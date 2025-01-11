package dev.wuason.adapter;

/**
 * The AdapterData record represents data associated with an adapter, including its instance, ID, and type.
 * It is used to encapsulate and manage information related to a specific adapter instance and its attributes.
 */
public record AdapterData(AdapterComp adapter, String id, String type) {
    /**
     * Determines whether another object is equal to this AdapterData instance.
     * Two AdapterData objects are considered equal if they have the same adapter instance,
     * and their id fields are equal an ignoring case.
     *
     * @param obj the object to compare with this instance for equality
     * @return true if the specified object is equal to this instance, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof AdapterData adapterData) {
            return adapter.equals(adapterData.adapter) && id.equalsIgnoreCase(adapterData.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return adapter.hashCode() + id.hashCode();
    }
}
