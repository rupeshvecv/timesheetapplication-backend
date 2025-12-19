package com.timesheetapplication.service;

import com.timesheetapplication.client.UserServiceFeignClient;
import com.timesheetapplication.projection.TimesheetFillingReportProjection;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelGenerator {

	@Autowired
	private UserServiceFeignClient userFeignClient;

	public ByteArrayInputStream exportReportToFilledUserExcel(List<TimesheetFillingReportProjection> report)
			throws IOException {

		try (XSSFWorkbook workbook = new XSSFWorkbook()) {

			Sheet sheet = workbook.createSheet("Timesheet Filling Report");
			String[] columns = { "Username", "Department", "Emp Code", "Filled Days", "Percentage Filled",
					"Total Days" };

			// Header style
			Row headerRow = sheet.createRow(0);
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerStyle.setFont(headerFont);

			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerStyle);
			}

			int rowIndex = 1;

			for (TimesheetFillingReportProjection r : report) {

				// Fetch dept & empCode via Feign client (may return null)
				String department;
				String empCode;
				String email;
				String userFullName;
				try {
					department = userFeignClient.getDepartmentByUsername(r.getUserName());
				} catch (Exception ex) {
					department = "--";
				}
				try {
					empCode = userFeignClient.getEmpCodeByUsername(r.getUserName());
				} catch (Exception ex) {
					empCode = "--";
				}
				try {
					email = userFeignClient.getEmailByUsername(r.getUserName());
					// userFullName =
					// userFeignClient.getFullNameByEmail(r.getUserName()+"@vecv.in");
					userFullName = userFeignClient.getFullNameByEmail(email);
				} catch (Exception ex) {
					userFullName = "--";
				}

				Row row = sheet.createRow(rowIndex++);
				row.createCell(0).setCellValue(userFullName != null ? userFullName : "--");
				/// row.createCell(0).setCellValue(r.getUserName() != null ? r.getUserName() :
				/// "--");
				row.createCell(1).setCellValue(department != null ? department : "--");
				row.createCell(2).setCellValue(empCode != null ? empCode : "--");

				// Filled days and totalDays are numbers
				Cell filledCell = row.createCell(3);
				filledCell.setCellValue(r.getFilledDays() != null ? r.getFilledDays() : 0L);

				Cell pctCell = row.createCell(4);
				pctCell.setCellValue(r.getPercentageFilled() != null ? r.getPercentageFilled() : 0.0);

				Cell totalCell = row.createCell(5);
				totalCell.setCellValue(r.getTotalDays() != null ? r.getTotalDays() : 0L);
			}

			// Auto-size columns
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			// =========================
			// CONDITIONAL FORMATTING %
			// =========================
			if (rowIndex > 1) {
				SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
				CellRangeAddress percentageRange = new CellRangeAddress(1, rowIndex - 1, 4, 4); // col index 4

				// Rule GREEN >= 90
				ConditionalFormattingRule ruleGreen = sheetCF.createConditionalFormattingRule(">=90");
				PatternFormatting fillGreen = ruleGreen.createPatternFormatting();
				fillGreen.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				// use .getCode() to pass short pattern value
				fillGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND.getCode());

				// Rule YELLOW between 50 (inclusive) and 90 (exclusive)
				ConditionalFormattingRule ruleYellow = sheetCF.createConditionalFormattingRule("AND(>=50,<90)");
				PatternFormatting fillYellow = ruleYellow.createPatternFormatting();
				fillYellow.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
				fillYellow.setFillPattern(FillPatternType.SOLID_FOREGROUND.getCode());

				// Rule RED < 50
				ConditionalFormattingRule ruleRed = sheetCF.createConditionalFormattingRule("<50");
				PatternFormatting fillRed = ruleRed.createPatternFormatting();
				fillRed.setFillForegroundColor(IndexedColors.ROSE.getIndex());
				fillRed.setFillPattern(FillPatternType.SOLID_FOREGROUND.getCode());

				ConditionalFormattingRule[] cfRules = new ConditionalFormattingRule[] { ruleGreen, ruleYellow,
						ruleRed };
				CellRangeAddress[] regions = new CellRangeAddress[] { percentageRange };

				sheetCF.addConditionalFormatting(regions, cfRules);
			}

			// Write workbook to byte array and return stream
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} // Workbook auto-closed by try-with-resources
	}
	
	public ByteArrayInputStream exportReportToFilledUserExcelOLD(List<TimesheetFillingReportProjection> report)
			throws IOException {

		try (XSSFWorkbook workbook = new XSSFWorkbook()) {

			Sheet sheet = workbook.createSheet("Timesheet Filling Report");
			String[] columns = { "Username", "Department", "Emp Code", "Filled Days", "Percentage Filled",
					"Total Days" };

			// Header style
			Row headerRow = sheet.createRow(0);
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerStyle.setFont(headerFont);

			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerStyle);
			}

			int rowIndex = 1;

			for (TimesheetFillingReportProjection r : report) {

				// Fetch dept & empCode via Feign client (may return null)
				String department;
				String empCode;
				String email;
				String userFullName;
				try {
					department = userFeignClient.getDepartmentByUsername(r.getUserName());
				} catch (Exception ex) {
					department = "--";
				}
				try {
					empCode = userFeignClient.getEmpCodeByUsername(r.getUserName());
				} catch (Exception ex) {
					empCode = "--";
				}
				try {
					email = userFeignClient.getEmailByUsername(r.getUserName());
					// userFullName =
					// userFeignClient.getFullNameByEmail(r.getUserName()+"@vecv.in");
					userFullName = userFeignClient.getFullNameByEmail(email);
				} catch (Exception ex) {
					userFullName = "--";
				}

				Row row = sheet.createRow(rowIndex++);
				row.createCell(0).setCellValue(userFullName != null ? userFullName : "--");
				/// row.createCell(0).setCellValue(r.getUserName() != null ? r.getUserName() :
				/// "--");
				row.createCell(1).setCellValue(department != null ? department : "--");
				row.createCell(2).setCellValue(empCode != null ? empCode : "--");

				// Filled days and totalDays are numbers
				Cell filledCell = row.createCell(3);
				filledCell.setCellValue(r.getFilledDays() != null ? r.getFilledDays() : 0L);

				Cell pctCell = row.createCell(4);
				pctCell.setCellValue(r.getPercentageFilled() != null ? r.getPercentageFilled() : 0.0);

				Cell totalCell = row.createCell(5);
				totalCell.setCellValue(r.getTotalDays() != null ? r.getTotalDays() : 0L);
			}

			// Auto-size columns
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}

			// =========================
			// CONDITIONAL FORMATTING %
			// =========================
			if (rowIndex > 1) {
				SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();
				CellRangeAddress percentageRange = new CellRangeAddress(1, rowIndex - 1, 4, 4); // col index 4

				// Rule GREEN >= 90
				ConditionalFormattingRule ruleGreen = sheetCF.createConditionalFormattingRule(">=90");
				PatternFormatting fillGreen = ruleGreen.createPatternFormatting();
				fillGreen.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
				// use .getCode() to pass short pattern value
				fillGreen.setFillPattern(FillPatternType.SOLID_FOREGROUND.getCode());

				// Rule YELLOW between 50 (inclusive) and 90 (exclusive)
				ConditionalFormattingRule ruleYellow = sheetCF.createConditionalFormattingRule("AND(>=50,<90)");
				PatternFormatting fillYellow = ruleYellow.createPatternFormatting();
				fillYellow.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
				fillYellow.setFillPattern(FillPatternType.SOLID_FOREGROUND.getCode());

				// Rule RED < 50
				ConditionalFormattingRule ruleRed = sheetCF.createConditionalFormattingRule("<50");
				PatternFormatting fillRed = ruleRed.createPatternFormatting();
				fillRed.setFillForegroundColor(IndexedColors.ROSE.getIndex());
				fillRed.setFillPattern(FillPatternType.SOLID_FOREGROUND.getCode());

				ConditionalFormattingRule[] cfRules = new ConditionalFormattingRule[] { ruleGreen, ruleYellow,
						ruleRed };
				CellRangeAddress[] regions = new CellRangeAddress[] { percentageRange };

				sheetCF.addConditionalFormatting(regions, cfRules);
			}

			// Write workbook to byte array and return stream
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		} // Workbook auto-closed by try-with-resources
	}

	public ByteArrayInputStream exportReportToUserProjectExcel(List<Map<String, Object>> data,
			List<String> projectNames) throws IOException {

		try (Workbook workbook = new XSSFWorkbook()) {

			Sheet sheet = workbook.createSheet("Timesheet User Project Report");

			Row header = sheet.createRow(0);
			int colIndex = 0;

			header.createCell(colIndex++).setCellValue("User Name");
			header.createCell(colIndex++).setCellValue("EmpCode");
			header.createCell(colIndex++).setCellValue("Designation");
			header.createCell(colIndex++).setCellValue("Department");
			header.createCell(colIndex++).setCellValue("Status");

			for (String proj : projectNames) {
				header.createCell(colIndex++).setCellValue(proj);
			}

			header.createCell(colIndex).setCellValue("Total");

			int rowIndex = 1;
			Map<String, Double> columnTotals = new LinkedHashMap<>();
			for (String proj : projectNames)
				columnTotals.put(proj, 0.0);

			double grandTotal = 0.0;

			for (Map<String, Object> row : data) {
				Row excelRow = sheet.createRow(rowIndex++);
				colIndex = 0;

				excelRow.createCell(colIndex++).setCellValue(row.get("UserName").toString());
				excelRow.createCell(colIndex++).setCellValue(row.get("EmpCode").toString());
				excelRow.createCell(colIndex++).setCellValue(row.get("Designation").toString());
				excelRow.createCell(colIndex++).setCellValue(row.get("Department").toString());
				excelRow.createCell(colIndex++).setCellValue(row.get("Status").toString());

				for (String proj : projectNames) {
					Double value = row.get(proj) == null ? 0.0 : Double.valueOf(row.get(proj).toString());
					excelRow.createCell(colIndex++).setCellValue(value);
					columnTotals.put(proj, columnTotals.get(proj) + value);
				}

				Double rowTotal = Double.valueOf(row.get("Total").toString());
				excelRow.createCell(colIndex).setCellValue(rowTotal);
				grandTotal += rowTotal;
			}

			Row totalRow = sheet.createRow(rowIndex);
			colIndex = 0;
			totalRow.createCell(colIndex++).setCellValue("Column Total");
			totalRow.createCell(colIndex++).setCellValue(""); // EmpCode blank
			totalRow.createCell(colIndex++).setCellValue(""); // Designation blank
			totalRow.createCell(colIndex++).setCellValue(""); // Department blank
			totalRow.createCell(colIndex++).setCellValue(""); // Status blank

			for (String proj : projectNames) {
				totalRow.createCell(colIndex++).setCellValue(columnTotals.get(proj));
			}

			totalRow.createCell(colIndex).setCellValue(grandTotal);

			for (int i = 0; i <= projectNames.size() + 6; i++) {
				sheet.autoSizeColumn(i);
			}

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			workbook.write(out);
			return new ByteArrayInputStream(out.toByteArray());
		}
	}

}
