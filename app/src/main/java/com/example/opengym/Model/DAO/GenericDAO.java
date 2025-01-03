package com.example.opengym.Model.DAO;

public interface GenericDAO<T> {

    /**
     * Create an new entry on the database
     * @param entity new entity to add to the database
     * @param parentName name of the parent entity that contains the entity to be updated
     * can be <code>null</code> if it doesn't have it
     * @return row ID or -1 if an error curred
     */
    long create(T entity, String parentName);

    /**
     * Remove an entry from the database
     * @param name primary key of the entry
     * @param parentName name of the parent entity that contains the entity to be updated
     * can be <code>null</code> if it doesn't have it
     * @return numbers of rows affected by the operation
     */
    int delete(String name, String parentName);

    /**
     * Read an entry from the database,
     * @param name primary key of the entry
     * @param parentName name of the parent entity that contains the entity to be updated
     * can be null if it doesn't have it
     * @return an object of the requested type or <code>null</code> if the entry doesn't exist
     */
    T read(String name, String parentName);

    /**
     * Update an entry from the database
     * @param entity new entity with modified values
     * @param name name of the entity to be updated
     * @param parentName name of the parent entity that contains the entity to be updated
     * can be null if it doesn't have it
     * @return number of rows affected by the operation
     */
    int update(T entity, String name, String parentName);

    /**
     * Close the connection opened when creating object
     * that implements the interface.
     */
    void closeConnection();
}
