use hrms_db
select * from booking_queue
select * from employees
select * from booking_master
select * from booking_queue
select * from game_slots
select * from cycle_participation

select * from employees
where password like '%\%'

-- Source - https://stackoverflow.com/a/18623233
-- Posted by orgtigger, modified by community. See post 'Timeline' for change history
-- Retrieved 2026-02-12, License - CC BY-SA 3.0

select chk.definition
from sys.check_constraints chk
inner join sys.columns col
    on chk.parent_object_id = col.object_id
inner join sys.tables st
    on chk.parent_object_id = st.object_id
where 
st.name = 'booking_queue'
and col.column_id = chk.parent_column_id

-- Source - https://stackoverflow.com/a/56329211
-- Posted by Mitio
-- Retrieved 2026-02-12, License - CC BY-SA 4.0

sp_helpconstraint 'booking_master', 'nomsg'

delete from booking_queue
dbcc checkident('hrms_db.dbo.booking_queue', RESEED, 0)

([status]='INQUEUE' OR [status]='CANCLED' OR [status]='PREEMPTED' OR [status]='CONFIRMED')
([status]='WAITING' OR [status]='CANCLED' OR [status]='PREEMPTED' OR [status]='CONFIRMED')
([status]='WAITING' OR [status]='CANCELED' OR [status]='PREEMPTED' OR [status]='CONFIRMED')

---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------
-- Source - https://stackoverflow.com/a/156813
-- Posted by kristof, modified by community. See post 'Timeline' for change history
-- Retrieved 2026-02-12, License - CC BY-SA 3.0

-- disable all constraints
EXEC sp_MSForEachTable "ALTER TABLE ? NOCHECK CONSTRAINT all"

-- Source - https://stackoverflow.com/a/10523803
-- Posted by Jim, modified by community. See post 'Timeline' for change history
-- Retrieved 2026-02-12, License - CC BY-SA 4.0

SET ANSI_NULLS, QUOTED_IDENTIFIER ON;

-- delete data in all tables
EXEC sp_MSForEachTable "DELETE FROM ?"

-- enable all constraints
exec sp_MSForEachTable "ALTER TABLE ? WITH CHECK CHECK CONSTRAINT all"

-- Source - https://stackoverflow.com/a/156813
-- Posted by kristof, modified by community. See post 'Timeline' for change history
-- Retrieved 2026-02-12, License - CC BY-SA 3.0

EXEC sp_MSForEachTable "DBCC CHECKIDENT ( '?', RESEED, 0)"

select * from departments

select * from positions

-- Source - https://stackoverflow.com/a/56329211
-- Posted by Mitio
-- Retrieved 2026-02-12, License - CC BY-SA 4.0

sp_helpconstraint 'positions', 'nomsg'

USE hrms_db;
ALTER ROLE db_owner ADD MEMBER hrms_user;