package com.example.opengym.Model.DAO;

public interface GenericDAO<T> {

    /**
     * Create an new entry on the database
     * The implementation should handle identification
     * @param entity
     */
    void create(T entity);

    /**
     * Delete an existing entry on the database
     * The implementation should handle the identification
     * @param entity
     * @throws android.database.SQLException
     */
    void remove(T entity) throws android.database.SQLException;
}
