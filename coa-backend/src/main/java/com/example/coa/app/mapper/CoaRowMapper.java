package com.example.coa.app.mapper;

import com.example.coa.app.dto.CoaRecord;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CoaRowMapper implements RowMapper<CoaRecord> {

    @Override
    public CoaRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        CoaRecord coa = new CoaRecord();
        coa.setFileName(rs.getString("FILE_NAME"));
        coa.setFullText(rs.getString("FULL_TEXT"));
        coa.setLotNumber(rs.getString("BATCH_LOT_CODE"));

        if (rs.getTimestamp("LAST_MODIFIED") != null) {
            coa.setLastModified(
                    rs.getTimestamp("LAST_MODIFIED").toLocalDateTime()
            );
        }
        return coa;
    }
}