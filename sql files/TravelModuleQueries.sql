-- Source - https://stackoverflow.com/a/219510
-- Posted by Dave_H, modified by community. See post 'Timeline' for change history
-- Retrieved 2026-02-11, License - CC BY-SA 4.0

SELECT * 
  FROM hrms_db.INFORMATION_SCHEMA.ROUTINES
 WHERE ROUTINE_TYPE = 'PROCEDURE'

select * from tempdb.sys.objects

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

CREATE PROCEDURE pr_travelmod_insert_travel
	@destination VARCHAR(100),
	@depart_date DATE,
	@return_date DATE,
	@emp_id INT,
	@scheduler_id INT
AS
BEGIN
	SET NOCOUNT ON;
    SET XACT_ABORT ON;

	INSERT INTO travel_details (destination, depart_date, return_date, employee_id, scheduler_id)
	VALUES(@destination, @depart_date, @return_date, @emp_id, @scheduler_id);
END;

CREATE PROCEDURE pr_travelmod_edit_travel
	@travel_id INT,
	@destination VARCHAR(100) = NULL,
	@depart_date DATE = NULL,
	@return_date DATE = NULL,
	@emp_id INT = NULL,
	@scheduler_id INT = NULL
AS
BEGIN
	SET NOCOUNT ON;
    SET XACT_ABORT ON;

	UPDATE travel_details
	SET destination = COALESCE(@destination, destination),
	    depart_date = COALESCE(@depart_date,depart_date), 
		return_date= COALESCE(@return_date,return_date),
		employee_id= COALESCE(@emp_id, employee_id),
		scheduler_id = COALESCE(@scheduler_id, scheduler_id)
	WHERE travel_id = @travel_id
END;

CREATE PROCEDURE pr_travelmod_add_travel_documents
	@doc_type_id INT,
	@doc_url VARCHAR(300),
	@uploaded_by_id INT,
	@uploded_date DATE,
	@travel_id INT
AS
BEGIN
	SET NOCOUNT ON;
    SET XACT_ABORT ON;

	INSERT INTO travel_documents (document_type_id, document_url, uploaded_by_id, uploaded_date, travel_id)
	VALUES(@doc_type_id, @doc_url, @uploaded_by_id, @uploded_date, @travel_id);
END;

CREATE PROCEDURE pr_travelmod_edit_travel_document
	@travel_document_id INT,
	@doc_type_id INT = NULL,
	@doc_url VARCHAR(300) = NULL,
	@uploaded_by_id INT =NULL,
	@uploaded_date DATE = NULL,
	@travel_id INT = NULL
AS
BEGIN
	UPDATE travel_documents
	SET document_type_id = COALESCE(@doc_type_id, document_type_id),
		document_url = COALESCE(@doc_url, document_url), 
		uploaded_by_id = COALESCE(@uploaded_by_id, uploaded_by_id), 
		uploaded_date = COALESCE(@uploaded_date, uploaded_date), 
		travel_id = COALESCE(@travel_id, travel_id)
	WHERE travel_document_id = @travel_document_id
END;

CREATE PROCEDURE pr_travelmod_delete_travel
	@travel_id INT
AS
BEGIN
	SET NOCOUNT ON;
    SET XACT_ABORT ON;

	UPDATE travel_details
	SET is_deleted = 1
	WHERE travel_id = @travel_id

	UPDATE travel_documents 
	SET is_deleted = 1
	WHERE travel_id = @travel_id
END;

CREATE PROCEDURE pr_travelmod_delete_travel_document
	@travel_document_id INT
AS
BEGIN
	SET NOCOUNT ON;
    SET XACT_ABORT ON;

	UPDATE travel_documents 
	SET is_deleted = 1
	WHERE travel_document_id = @travel_document_id
END;

CREATE PROCEDURE pr_travelmod_add_expenses
	@expense_type_id INT,
	@travel_id INT,
	@amount DECIMAL(18,0),
	@comment VARCHAR(400),
	@proof_url VARCHAR(300),
	@upload_date DATE,
	@expense_date DATE
AS
BEGIN
	SET NOCOUNT ON;
    SET XACT_ABORT ON;

	INSERT INTO expenses_detail(expense_type_id, travel_id, amount, comment, proof_url, upload_date, expense_date)
	VALUES (@expense_type_id, @travel_id, @amount,@comment, @proof_url, @upload_date, @expense_date)
END;

CREATE PROCEDURE pr_travelmod_edit_expenses
	@expense_id INT,
	@expense_type_id INT = NULL,
	@travel_id INT = NULL,
	@amount DECIMAL(18,0) = NULL,
	@comment VARCHAR(400) = NULL,
	@proof_url VARCHAR(300) = NULL,
	@upload_date DATE = NULL,
	@expense_date DATE = NULL
AS 
BEGIN 
	UPDATE expenses_detail
	SET expense_type_id = COALESCE(@expense_type_id, expense_type_id),
		travel_id = COALESCE(@travel_id, travel_id),
		amount = COALESCE(@amount, amount),
		comment = COALESCE(@comment, comment),
		proof_url = COALESCE(@proof_url, proof_url), 
		upload_date = COALESCE(@upload_date, upload_date), 
		expense_date = COALESCE(@expense_date, expense_date)
	WHERE expense_id = @expense_id
END;

CREATE PROCEDURE pr_travelmod_add_expense_review
	@expense_id INT,
	@reviewed_by_id INT,
	@review_status_id INT,
	@comment VARCHAR(350) = NULL
AS
BEGIN
	INSERT INTO expenses_review(expense_id, reviewed_by_id, review_status_id, comment)
	VALUES(@expense_id, @reviewed_by_id, @review_status_id, @comment)
END;

CREATE PROCEDURE pr_travelmod_delete_expense
	@expense_id INT
AS
BEGIN
	UPDATE expenses_detail
	SET is_deleted = 1
	WHERE expense_id = @expense_id

	UPDATE expenses_review
	SET is_deleted = 1
	WHERE expense_id = @expense_id
END;

CREATE PROCEDURE pr_travelmod_get_document_types
AS
BEGIN
	SELECT document_type_id, document_type__name FROM document_types
END;

CREATE PROCEDURE pr_travelmod_get_expenses_type
AS
BEGIN
	SELECT expense_type_id, expence_type_name FROM expenses_type
END;

select * from travel_details