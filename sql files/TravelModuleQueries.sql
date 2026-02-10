CREATE PROCEDURE pr_travelmod_get_travel_details
AS
BEGIN
	SELECT travel_id, employees.full_name, destination, depart_date, return_date FROM travel_details
	JOIN employees
	ON travel_details.employee_id = employees.employee_id
END;

CREATE PROCEDURE pr_travelmod_get_travel_details_by_travel_id
	@travel_id INT
AS
BEGIN
	SELECT travel_id, employees.full_name, destination, depart_date, return_date FROM travel_details
	JOIN employees
	ON travel_details.employee_id = employees.employee_id
	WHERE travel_id = @travel_id
END;

CREATE PROCEDURE pr_travelmod_get_travels_by_employee_id
	@employee_id INT
AS
BEGIN
	SELECT travel_id, employees.full_name, destination, depart_date, return_date FROM travel_details
	JOIN employees
	ON travel_details.employee_id = employees.employee_id
	WHERE employees.employee_id = @employee_id
END;

CREATE PROCEDURE pr_travelmod_gell_all_travel_details
	@travel_id INT
AS
BEGIN
	SELECT travel_details.travel_id, 
		   employees.full_name, 
		   destination, 
		   depart_date, 
		   return_date, 
		   travel_documents.document_url,
		   uploader.full_name,
		   travel_documents.uploaded_date,
		   positions.position_name
	FROM travel_details
	JOIN employees
	ON travel_details.employee_id = employees.employee_id
	JOIN travel_documents
	ON travel_documents.travel_id = travel_details.travel_id
	JOIN document_types
	ON travel_documents.document_type_id = document_types.document_type_id
	JOIN employees uploader
	ON travel_documents.uploaded_by_id = uploader.employee_id
	JOIN positions
	ON uploader.position_id = positions.position_id
	WHERE travel_details.travel_id = @travel_id
END;

CREATE PROCEDURE pr_travelmod_get_expense_by_travel_id
	@travel_id INT
AS
BEGIN
	SELECT trv_dtl.travel_id, 
		   emp.full_name,
		   trv_dtl.travel_id,
		   exp_typ.expence_type_name,
		   exp_dt.amount,
		   exp_dt.comment,
		   exp_dt.proof_url,
		   exp_dt.upload_date,
		   exp_dt.expense_date
	FROM travel_details trv_dtl
	JOIN employees emp
	ON trv_dtl.employee_id = emp.employee_id
	JOIN expenses_detail exp_dt
	ON exp_dt.travel_id = trv_dtl.travel_id
	JOIN expenses_type exp_typ
	ON exp_dt.expense_type_id = exp_typ.expense_type_id
END;
