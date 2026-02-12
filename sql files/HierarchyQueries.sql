CREATE OR ALTER PROCEDURE pr_orgmod_get_hierarchy
	@emp_id INT
AS
BEGIN 
	DECLARE @supervisior_id INT;
	DECLARE @employee_id INT;
	PRINT 'HELLO';
	SELECT @supervisior_id = supervisior_id, @employee_id = employee_id
	FROM employees
	WHERE employee_id = @emp_id	

	CREATE TABLE #temp(employee_name VARCHAR(100), supervision_name VARCHAR(100), employee_position VARCHAR(100), supervisior_position VARCHAR(100), employee_pp VARCHAR(100), supervisior_pp VARCHAR(100));
	WHILE @supervisior_id <> @employee_id
	BEGIN
		INSERT INTO #temp 
		SELECT employees.full_name AS employee_name,
			   e.full_name AS supervision_name, 
			   positions.position_name,
			   p.position_name,
			   employees.profile_picture_url AS employee_pp,
			   e.profile_picture_url AS supervisior_pp
		FROM employees
		JOIN employees e
		ON employees.supervisior_id = e.employee_id
		JOIN positions
		ON employees.position_id = positions.position_id
		JOIN positions p
		ON e.position_id = p.position_id
		WHERE employees.employee_id = @employee_id
		PRINT @employee_id;
		PRINT @supervisior_id;
		SET @employee_id = (SELECT supervisior_id FROM employees WHERE employee_id = @employee_id)
		SET @supervisior_id = (SELECT supervisior_id FROM employees WHERE employee_id = @employee_id)
	
	END
	SELECT * FROM #temp
END


--CREATE FUNCTION GetHierarchy(@emp_id int)
--RETURNS TABLE( employee_name VARCHAR(100), supervision_name VARCHAR(100),  employee_pp VARCHAR(100), supervisior_pp VARCHAR(100));
--AS 
--BEGIN	
--DECLARE @supervisior_id INT;
--	DECLARE @employee_id INT;
--	create table #temp(employee_name VARCHAR(100), supervision_name VARCHAR(100),  employee_pp VARCHAR(100), supervisior_pp VARCHAR(100));
	
--	SELECT @supervisior_id = supervisior_id, @employee_id = employee_id
--	FROM employees
--	WHERE employee_id = @emp_id

--	WHILE @supervisior_id <> @employee_id
--	BEGIN
--		SELECT employees.full_name AS employee_name,
--			   e.full_name AS supervision_name, 
--			   employees.profile_picture_url AS employee_pp,
--			   e.profile_picture_url AS supervisior_pp
--		into #temp FROM employees
--		join positions
--		on employees.position_id = positions.position_id
--		JOIN employees e
--		ON employees.supervisior_id = e.employee_id
		
--		WHERE employees.employee_id = @employee_id
--		PRINT @employee_id;
--		PRINT @supervisior_id;
--		set @employee_id = (SELECT supervisior_id FROM employees WHERE employee_id = @employee_id)
--		set @supervisior_id = (SELECT supervisior_id FROM employees WHERE employee_id = @employee_id)
	
--	END
--END;

drop procedure test
as
begin
SELECT * FROM employees
end

exec pr_orgmod_get_hierarchy @emp_id = 10

EXEC sp_rename 'dbo.GetHierarchy', 'pr_orgmod_get_hierarchy';
EXEC sp_rename 'dbo.ApplyForSlot', 'pr_gamemod_slot_apply';
EXEC sp_rename 'dbo.CancleSlot', 'pr_gamemod_slot_cancle';