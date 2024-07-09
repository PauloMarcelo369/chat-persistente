package util;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonObject;

public interface Operation {
    public JsonObject toJsonObject(ResultSet resuSet) throws SQLException;
}
