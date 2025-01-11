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

    /**
     * Computes a hash code for this AdapterData instance.
     * The hash code is calculated based on the hash codes of the adapter and id fields.
     *
     * @return the hash code value for this AdapterData instance
     */
    @Override
    public int hashCode() {
        return adapter.hashCode() + id.hashCode();
    }

    /**
     * Returns a string*/
    @Override
    public String toString() {
        return Utils.convert(adapter.getType(), id);
    }

    /**
     * Retrieves the adapter identifier by combining the adapter type and ID into a single string representation.
     *
     * @return a string that represents the adapter identifier, formatted as "type:id"
     */
    public String getAdapterId() {
        return toString();
    }
}
