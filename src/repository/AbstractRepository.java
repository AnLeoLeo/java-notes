package repository;

import db.Db;

import java.sql.ResultSet;
import java.sql.SQLException;

abstract public class AbstractRepository {

    protected Db db;

    public AbstractRepository(Db db) {
        this.db = db;

        this.initFakeData();
    }

    abstract protected void initFakeData();
}
