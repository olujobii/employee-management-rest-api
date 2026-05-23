ALTER TABLE employees
ADD CHECK ( date_of_joining <= CURRENT_DATE );