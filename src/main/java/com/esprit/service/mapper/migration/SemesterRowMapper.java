package com.esprit.service.mapper.migration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.esprit.dto.PeriodDTO;
import com.esprit.dto.SemesterDTO;

public class SemesterRowMapper implements RowMapper<SemesterDTO> {

	private boolean semesterDoesNotExist;

	private List<SemesterDTO> semesters = new ArrayList<>();

	@Override
	public SemesterDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		semesterDoesNotExist = true;
		String semesterId = rs.getString("ID_SEM");

		List<PeriodDTO> periods = new ArrayList<>();
		periods.add(new PeriodDTO().periodId(rs.getString("ID_PER")).code(rs.getString("COD_PER"))
				.startDate(rs.getDate("DATE_DEB_PER")).endDate(rs.getDate("DAT_FIN_PER")));

		semesters.forEach(semester -> {
			if (semester.getSemestreId().equals(semesterId)) {
				semester.getPeriods().addAll(periods);
				semesterDoesNotExist = false;
			}
		});

		if (semesterDoesNotExist) {
			semesters.add(new SemesterDTO().semestreId(semesterId).code(rs.getString("COD_SEM"))
					.startDate(rs.getDate("DATE_DEB_SEM")).endDate(rs.getDate("DAT_FIN_SEM")).periods(periods));
		}
		return semesters.get(0);
	}

}