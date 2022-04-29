SET MODE MSSQLServer;


SET @ID_MEGA = 52;
SET @Person_Minor = 22187;
SET @Person_Major = 217303;


SELECT Person_Fullname
	,Person_Birth
	,Person_ActAddress
	,Person_MobPhone
	,Person_ID_MEGA
	,Person_Id_Minor
	,Person_Id_Major
	,Deposit_ID_MEGA
	,Deposit_PrintableNo
	,Deposit_Id_Minor
	,Deposit_Id_Major
	,Deposit_Person_Minor
	,Deposit_Person_Major
	,Deposit_BranchNo
	,Deposit_Office
	,Deposit_Kind
	,Deposit_Subkind
	,Deposit_OpenDay
	,Deposit_State
	,Deposit_Currency
FROM CLIENT.Person pers WITH (NOLOCK)
	INNER JOIN DEPOSIT.Deposit dep WITH (NOLOCK)
		ON (pers.Person_ID_MEGA = dep.Deposit_ID_MEGA)
			AND (Person_Id_Minor = dep.Deposit_Person_Minor)
			AND (Person_Id_Major = dep.Deposit_Person_Major)
