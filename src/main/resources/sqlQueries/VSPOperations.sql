SET MODE MSSQLServer;


SET @beg_day = '2021-07-01';
SET @end_day = '2021-10-08';
SET @ID_MEGA = 16;
SET @BranchNo = 8597;
SET @Office = 560;


CREATE LOCAL TEMPORARY TABLE IF NOT EXISTS CurrList (
    Currency    INT,
    Code		NVARCHAR(10),
    Name		NVARCHAR(30),
    Cash		DECIMAL(15,2),
    Ind         DECIMAL(15,2),
    CashOne     DECIMAL(15,2)
);

INSERT INTO CurrList VALUES (33, N'A33', N'Палладий', 5925.29, 01.00, 5925.29);
INSERT INTO CurrList VALUES (36, N'AUD', N'Австралийский доллар', 56.13, 01.00, 56.13);
INSERT INTO CurrList VALUES (76, N'A76', N'Платина', 2169.26, 01.00, 2169.26);
INSERT INTO CurrList VALUES (98, N'A98', N'золото', 4740.23, 01.00, 4740.23);
INSERT INTO CurrList VALUES (99, N'A99', N'Серебро', 59.08, 01.00, 59.08);
INSERT INTO CurrList VALUES (124, N'CAD', N'Канадский доллар', 58.87, 01.00, 58.87);
INSERT INTO CurrList VALUES (156, N'CNY', N'Юань', 11.51, 01.00, 11.51);
INSERT INTO CurrList VALUES (208, N'DKK', N'Датская крона', 12.33, 01.00, 12.33);
INSERT INTO CurrList VALUES (344, N'HKD', N'Гонконгский доллар', 10.08, 01.00, 10.08);
INSERT INTO CurrList VALUES (392, N'JPY', N'Иена', 73.94, 100.00, 00.74);
INSERT INTO CurrList VALUES (578, N'NOK', N'Норвежская крона', 84.17, 10.00, 08.42);
INSERT INTO CurrList VALUES (702, N'SGD', N'Сингапурский доллар', 57.36, 01.00, 57.36);
INSERT INTO CurrList VALUES (752, N'SEK', N'Шведская крона', 87.74, 10.00, 08.77);
INSERT INTO CurrList VALUES (756, N'CHF', N'Швейцарский франк', 85.13, 01.00, 85.13);
INSERT INTO CurrList VALUES (810, N'RUR', N'Рубль', 01.00, 01.00, 01.00);
INSERT INTO CurrList VALUES (826, N'GBP', N'Фунт стерлингов', 101.16, 01.00, 101.16);
INSERT INTO CurrList VALUES (840, N'USD', N'Доллар США', 78.13, 01.00, 78.13);
INSERT INTO CurrList VALUES (978, N'EUR', N'Евро', 91.66, 01.00, 91.66);


CREATE LOCAL TEMPORARY TABLE IF NOT EXISTS JBT_Operation_ClerkOP06 (
    ID_MEGA			TINYINT,
    BranchNo		INT,
    OfficeNo		INT,
    PayTime			TIME(0),
    OpDay			DATE,
    OpNo			INT,
    CashierNo		INT,
    CounterNo		INT,
    StornoSign		SMALLINT,
    StornoTime		TIME(0),
    jcl_SurName		NVARCHAR(60),
    jcl_FirstName	NVARCHAR(20),
    jcl_SecondName	NVARCHAR(20),
    jcli_SurName	NVARCHAR(60),
    jcli_FirstName	NVARCHAR(20),
    jcli_SecondName	NVARCHAR(20),
    jcl_BirthDay	DATE,
    jcli_BirthDay	DATE,
    IdOperation		BIGINT,
    OpCode			SMALLINT,
    PRINTABLENO		NVARCHAR(256)
);

INSERT INTO JBT_Operation_ClerkOP06
SELECT jo.ID_MEGA					AS ID_MEGA
    ,jo.BranchNo					AS BranchNo
    ,jo.OfficeNo					AS OfficeNo
    ,jo.PayTime						AS PayTime
    ,jo.OpDay						AS OpDay
    ,jo.OpNo						AS OpNo
    ,jo.CashierNo					AS CashierNo
    ,jo.CounterNo					AS CounterNo
    ,jo.StornoSign					AS StornoSign
    ,jo.StornoTime					AS StornoTime
    ,jcl.SurName					AS jcl_SurName
    ,jcl.FirstName					AS jcl_FirstName
    ,jcl.SecondName					AS jcl_SecondName
    ,jcli.SurName					AS jcli_SurName
    ,jcli.FirstName					AS jcli_FirstName
    ,jcli.SecondName				AS jcli_SecondName
    ,jcl.BirthDay					AS jcl_BirthDay
    ,jcli.BirthDay					AS jcli_BirthDay
    ,jo.IdOperation
    ,jo.OpCode
    ,(SELECT TOP 1 ao.Value
        FROM JBT.OperationAttribute ao WITH (NOLOCK)
        WHERE (ao.IdOperation = jo.IdOperation)
            AND	(ao.OpDay = jo.OpDay)
            AND (ao.Code = ISNULL(ISNULL((SELECT TOP 1 cop.CodeAtr
                FROM OARB.CodeAccDictionary cop WITH (NOLOCK)
                WHERE (cop.OpCode = jo.OpCode)
            ), 1105), 1717))
            AND (ao.Value IS NOT NULL)
    )                               AS PRINTABLENO
FROM JBT.Operation jo WITH (NOLOCK)
    LEFT JOIN IRBIS.NCIOP n WITH (NOLOCK)
        ON (n.ID_MEGA = jo.ID_MEGA)
            AND (n.SHFR0 = jo.OpCode)
    LEFT JOIN JBT.Client jcl WITH (NOLOCK)
        ON (jo.IdOperation = jcl.IdOperation)
            AND (jo.OpDay = jcl.OpDay)
            AND (jcl.DocCode = 114)
    LEFT JOIN JBT.Client jcli WITH (NOLOCK)
        ON (jcli.IdOperation = jo.IdOperation)
            AND (jcli.OpDay = jo.OpDay)
            AND (jcli.Type = 3)
WHERE (jo.ID_MEGA = @ID_MEGA)
    AND (jo.BranchNo = @BranchNo)
    AND (jo.OfficeNo = @Office)
    AND (jo.OpDay BETWEEN @beg_day AND @end_day);


CREATE LOCAL TEMPORARY TABLE IF NOT EXISTS JBT_OperationAcc_ClerkOP06 (
    ID_MEGA         TINYINT,
    BranchNo        INT,
    OfficeNo        INT,
    PayTime         TIME(0),
    OpDay           DATE,
    OpNo            INT,
    CashierNo       INT,
    CounterNo       INT,
    StornoSign	    SMALLINT,
    StornoTime	    TIME(0),
    jcl_SurName		NVARCHAR(60),
    jcl_FirstName	NVARCHAR(20),
    jcl_SecondName	NVARCHAR(20),
    jcli_SurName	NVARCHAR(60),
    jcli_FirstName	NVARCHAR(20),
    jcli_SecondName	NVARCHAR(20),
    jcl_BirthDay	DATE,
    jcli_BirthDay	DATE,
    IdOperation		BIGINT,
    OpCode			SMALLINT,
    PRINTABLENO		NVARCHAR(40),
    DEPOSIT_MINOR	INT,
    DEPOSIT_MAJOR	INT
);

INSERT INTO JBT_OperationAcc_ClerkOP06
SELECT s.*
FROM(
    SELECT o.ID_MEGA
        ,o.BranchNo
        ,o.OfficeNo
        ,o.PayTime
        ,o.OpDay
        ,o.OpNo
        ,o.CashierNo
        ,o.CounterNo
        ,o.StornoSign
        ,o.StornoTime
        ,o.jcl_SurName
        ,o.jcl_FirstName
        ,o.jcl_SecondName
        ,o.jcli_SurName
        ,o.jcli_FirstName
        ,o.jcli_SecondName
        ,o.jcl_BirthDay
        ,o.jcli_BirthDay
        ,o.IdOperation
        ,o.OpCode
        ,dd.DEPOSIT_PRINTABLENO     AS PRINTABLENO
        ,dd.DEPOSIT_ID_MINOR        AS DEPOSIT_MINOR
        ,dd.DEPOSIT_ID_MAJOR        AS DEPOSIT_MAJOR
    FROM DEPOSIT.DEPOSIT dd WITH (NOLOCK)
        INNER JOIN JBT_Operation_ClerkOP06 o
            ON (REPLACE(o.PRINTABLENO, 'A', '0') = dd.DEPOSIT_PRINTABLENO)
                AND (o.ID_MEGA = dd.DEPOSIT_ID_MEGA)
    ) s
WHERE (s.PRINTABLENO IS NOT NULL);


CREATE LOCAL TEMPORARY TABLE IF NOT EXISTS DEPOHEIR_ClerkOP06 (
    ID_MEGA           TINYINT,
    DEPOSIT_MINOR     INT,
    DEPOSIT_MAJOR     INT,
    No                INT,
    FULLNAME	      NVARCHAR(122),
    Birth             DATE,
    ActAddress        NVARCHAR(80),
    PRINTABLENO       NVARCHAR(40)
);

INSERT INTO DEPOHEIR_ClerkOP06
SELECT DISTINCT a.ID_MEGA
    ,a.DEPOSIT_MINOR
    ,a.DEPOSIT_MAJOR
    ,dhh.DEPOHEIR_No
    ,dhhp.PERSON_FULLNAME
    ,dhhp.PERSON_Birth
    ,dhhp.PERSON_ActAddress
    ,a.PRINTABLENO
FROM JBT_OperationAcc_ClerkOP06 a
    INNER JOIN Deposit.DEPOHEIR dhh WITH (NOLOCK)
        ON (dhh.DEPOHEIR_DEPOSIT_MINOR = a.DEPOSIT_MINOR)
            AND (dhh.DEPOHEIR_DEPOSIT_MAJOR = a.DEPOSIT_MAJOR)
            AND (dhh.DEPOHEIR_ID_MEGA = a.ID_MEGA)
    INNER JOIN CLIENT.PERSON dhhp WITH (NOLOCK)
        ON (dhhp.PERSON_ID_MEGA = dhh.DEPOHEIR_ID_MEGA)
            AND (dhhp.PERSON_ID_MINOR = dhh.DEPOHEIR_PERSON_MINOR)
            AND (dhhp.PERSON_ID_MAJOR = dhh.DEPOHEIR_PERSON_MAJOR);


CREATE LOCAL TEMPORARY TABLE IF NOT EXISTS DEPOTRUSTED_ClerkOP06 (
    ID_MEGA           TINYINT,
    DEPOSIT_MINOR     INT,
    DEPOSIT_MAJOR     INT,
    No                INT,
    FULLNAME          NVARCHAR(122),
    Birth             DATE,
    ActAddress        NVARCHAR(80),
    PRINTABLENO       NVARCHAR(40)
);

INSERT INTO DEPOTRUSTED_ClerkOP06
SELECT DISTINCT a.ID_MEGA
    ,a.DEPOSIT_MINOR
    ,a.DEPOSIT_MAJOR
    ,dt.DEPOTRUSTED_No
    ,dtp.PERSON_FULLNAME
    ,dtp.PERSON_Birth
    ,dtp.PERSON_ActAddress
    ,a.PRINTABLENO
FROM JBT_OperationAcc_ClerkOP06 a
    INNER JOIN Deposit.DepoTrusted dt WITH (NOLOCK)
        ON (dt.DEPOTRUSTED_DEPOSIT_MINOR = a.DEPOSIT_MINOR)
            AND (dt.DEPOTRUSTED_DEPOSIT_MAJOR = a.DEPOSIT_MAJOR)
            AND (dt.DEPOTRUSTED_ID_MEGA = a.ID_MEGA)
    INNER JOIN CLIENT.Person dtp WITH (NOLOCK)
        ON (dtp.PERSON_ID_MEGA = dt.DEPOTRUSTED_ID_MEGA)
            AND (dtp.PERSON_ID_MINOR = dt.DEPOTRUSTED_PERSON_MINOR)
            AND (dtp.PERSON_ID_MAJOR = dt.DEPOTRUSTED_PERSON_MAJOR);


CREATE LOCAL TEMPORARY TABLE IF NOT EXISTS DEPOINVESTOR_ClerkOP06 (
    ID_MEGA           TINYINT,
    DEPOSIT_MINOR     INT,
    DEPOSIT_MAJOR     INT,
    No                INT,
    FULLNAME          NVARCHAR(122),
    Birth             DATE,
    ActAddress        NVARCHAR(80),
    PRINTABLENO       NVARCHAR(40)
);

INSERT INTO DEPOINVESTOR_ClerkOP06
SELECT DISTINCT a.ID_MEGA
    ,a.DEPOSIT_MINOR
    ,a.DEPOSIT_MAJOR
    ,di.DEPOINVESTOR_No
    ,dip.PERSON_FULLNAME
    ,dip.PERSON_Birth
    ,dip.PERSON_ActAddress
    ,a.PRINTABLENO
FROM JBT_OperationAcc_ClerkOP06 a
    LEFT JOIN Deposit.DepoInvestor di WITH (NOLOCK)
        ON (di.DEPOINVESTOR_DEPOSIT_MINOR = a.DEPOSIT_MINOR)
            AND (di.DEPOINVESTOR_DEPOSIT_MAJOR = a.DEPOSIT_MAJOR)
            AND (di.DEPOINVESTOR_ID_MEGA = a.ID_MEGA)
    LEFT JOIN CLIENT.Person dip WITH (NOLOCK)
        ON (dip.PERSON_ID_MEGA = di.DEPOINVESTOR_ID_MEGA)
            AND (dip.PERSON_ID_MINOR = di.DEPOINVESTOR_PERSON_MINOR)
            AND (dip.PERSON_ID_MAJOR = di.DEPOINVESTOR_PERSON_MAJOR);


SELECT dh.DEPOHIST_ID_MEGA			AS ID_MEGA
    ,dp.PERSON_FULLNAME             AS [ФИО вкладчика]
    ,dp.PERSON_Birth				AS [Дата рождения вкладчика]
    ,dd.DEPOSIT_BranchNo			AS [ОСБ счета]
    ,dd.DEPOSIT_Office				AS [ВСП счета]
    ,dd.DEPOSIT_PRINTABLENO			AS [Номер счета]
    ,dd.DEPOSIT_Kind				AS [Вид счета]
    ,dd.DEPOSIT_SubKind				AS [Подвид счета]
    ,dd.DEPOSIT_OpenDay				AS [Дата открытия счета]
    ,dd.DEPOSIT_State				AS [Состояние счета]
    ,dh.DEPOHIST_BranchNo			AS [ОСБ операции]
    ,dh.DEPOHIST_Office				AS [ВСП операции]
    ,dh.DEPOHIST_State				AS [Признак сторнирования]
    ,o.StornoTime                   AS [Опер.Сторно Время]
    ,ISNULL(o.OpDay, dh.DEPOHIST_OpDay) AS [Дата операции]
    ,o.PayTime                      AS [Опер.Время]
    ,dh.DEPOHIST_JrnNo              AS [Номер операции по ЖБТ]
    ,dh.DEPOHIST_OpNo               AS [Номер операции по счету]
    ,dh.DEPOHIST_TurnCode           AS Оборот
    ,dh.DEPOHIST_OpKind             AS [Вид операции]
    ,dh.DEPOHIST_OpCode             AS [Код операции]
    ,dh.DEPOHIST_OpCash             AS [Сумма операции]
    ,dh.DEPOHIST_Balance            AS [Остаток счета]
    ,dh.DEPOHIST_PinAcceptFlag      AS [Признак ПИН]
    ,o.CounterNo                    AS [Жетон N]
    ,o.CashierNo                    AS [Кассир N]
    ,(
        SELECT TOP(1) cl.FIO
        FROM DEPOSIT.Clerk_FIO AS cl WITH (NOLOCK)
        WHERE (cl.BRANCHNO = o.BranchNo)
            AND (cl.OFFICENO = o.OfficeNo)
            AND (cl.CLERK = o.CashierNo)
            AND (cl.ID_MEGA = o.ID_MEGA)
            AND (cl.DATEOPEN = (
                SELECT MAX(cl2.DATEOPEN)
                FROM DEPOSIT.Clerk_FIO cl2 WITH (NOLOCK)
                WHERE (cl2.DATEOPEN <= o.OpDay)
                    AND (cl2.BRANCHNO = o.BranchNo)
                    AND (cl2.OFFICENO = o.OfficeNo)
                    AND (cl2.CLERK = o.CashierNo)
                    AND (cl2.ID_MEGA = o.ID_MEGA)
            )
        )
    )                               AS [Кассир ФИО]
    ,dh.DEPOHIST_DebitAccount       AS [Опер.Сч.по Дебету]
    ,ddp.PERSON_FullName            AS [Опер.ФИО по Дебету]
    ,ddp.PERSON_Birth               AS [Опер.ДР по Дебету]
    ,dh.DEPOHIST_CreditAccount      AS [Опер.Сч.по Кредиту]
    ,dсp.PERSON_FullName            AS [Опер.ФИО по Кредиту]
    ,dсp.PERSON_Birth               AS [Опер.ДР по Кредиту]
    ,dh.DEPOHIST_HeirNo             AS [Кем совершена]
    ,CASE
        WHEN (dh.DEPOHIST_HeirNo = 0) THEN N'Вкладчик'
        WHEN (dh.DEPOHIST_HeirNo > 0) AND (dh.DEPOHIST_HeirNo < 51) THEN N'Наследник'
        WHEN (dh.DEPOHIST_HeirNo > 50) AND (dh.DEPOHIST_HeirNo < 101) THEN N'Доверенное лицо'
        WHEN (dh.DEPOHIST_HeirNo > 100) THEN N'Вноситель'
        WHEN (dh.DEPOHIST_HeirNo = -1) THEN N'Вкладчик перевод'
        ELSE N'Не определено'
    END                                 AS [Операцию совершил]
    ,CASE
        WHEN (dh.DEPOHIST_HeirNo = 0) THEN dp.PERSON_FULLNAME
        WHEN (dh.DEPOHIST_HeirNo > 0) AND (dh.DEPOHIST_HeirNo <= 50) THEN
            CASE
                WHEN (o.jcl_SurName IS NULL) THEN dhh.FULLNAME
                ELSE CONCAT(o.jcl_SurName, ' ', o.jcl_FirstName, ' ', o.jcl_SecondName)
            END
        WHEN (dh.DEPOHIST_HeirNo > 50) AND (dh.DEPOHIST_HeirNo <= 100) THEN
            CASE
                WHEN (o.jcl_SurName IS NULL) THEN dht.FULLNAME
                ELSE CONCAT(o.jcl_SurName, ' ', o.jcl_FirstName, ' ', o.jcl_SecondName)
            END
        WHEN (dh.DEPOHIST_HeirNo > 100) THEN
            CASE
                WHEN (o.jcli_SurName IS NULL) THEN dhi.FULLNAME
                ELSE CONCAT(o.jcli_SurName, ' ', o.jcli_FirstName, ' ', o.jcli_SecondName)
            END
        WHEN (dh.DEPOHIST_HeirNo =-1 ) THEN
            CASE
                WHEN (o.jcli_SurName IS NULL) THEN dht.FULLNAME
                ELSE CONCAT(o.jcli_SurName, ' ', o.jcli_FirstName, ' ', o.jcli_SecondName)
            END
        ELSE dp.PERSON_FULLNAME
    END                                 AS [ФИО клиента совершившего операцию]
    ,CASE
        WHEN (dh.DEPOHIST_HeirNo = 0) THEN dp.PERSON_Birth
        WHEN (dh.DEPOHIST_HeirNo > 0) AND (dh.DEPOHIST_HeirNo <= 50) THEN
            CASE
                WHEN (o.jcl_BirthDay IS NULL) THEN dhh.BIRTH
                ELSE o.jcl_BirthDay
            END
        WHEN (dh.DEPOHIST_HeirNo > 50) AND (dh.DEPOHIST_HeirNo <= 100) THEN
            CASE
                WHEN (o.jcl_BirthDay IS NULL) THEN dht.BIRTH
                ELSE o.jcl_BirthDay
            END
        WHEN (dh.DEPOHIST_HeirNo > 100) THEN
            CASE
                WHEN (o.jcli_BirthDay IS NULL) THEN dhi.Birth
                ELSE o.jcli_BirthDay
            END
        WHEN (dh.DEPOHIST_HeirNo = -1) THEN
            CASE
                WHEN (o.jcli_BirthDay IS NULL) THEN dht.Birth
                ELSE o.jcli_BirthDay
            END
        ELSE dp.PERSON_Birth
    END                                 AS [ДР клиента совершившего операцию]
    ,CASE
        WHEN (dh.DEPOHIST_HeirNo = 0) THEN dp.PERSON_ActAddress
        WHEN (dh.DEPOHIST_HeirNo > 0) AND (dh.DEPOHIST_HeirNo <= 50) THEN dhh.ActAddress
        WHEN (dh.DEPOHIST_HeirNo > 50) AND (dh.DEPOHIST_HeirNo <= 100) THEN dht.ActAddress
        WHEN (dh.DEPOHIST_HeirNo > 100) THEN dhi.ActAddress
        ELSE dp.PERSON_ActAddress
    END                                 AS [Адрес клиента совершившего операцию]
    ,dh.DEPOHIST_Clerk                  AS Оператор
    ,(
        SELECT TOP(1) cl.FIO
        FROM DEPOSIT.Clerk_FIO AS cl WITH (NOLOCK)
        WHERE (cl.BRANCHNO = dh.DEPOHIST_BranchNo)
            AND (cl.OFFICENO = dh.DEPOHIST_Office)
            AND (cl.CLERK = dh.DEPOHIST_Clerk)
            AND (cl.ID_MEGA = dh.DEPOHIST_ID_MEGA)
            AND (cl.DATEOPEN =(
                SELECT MAX(cl2.DATEOPEN)
                FROM DEPOSIT.Clerk_FIO cl2 WITH (NOLOCK)
                WHERE (cl2.DATEOPEN <= dh.DEPOHIST_OpDay)
                    AND (cl2.BRANCHNO = dh.DEPOHIST_BranchNo)
                    AND (cl2.OFFICENO = dh.DEPOHIST_Office)
                    AND (cl2.CLERK = dh.DEPOHIST_Clerk)
                    AND (cl2.ID_MEGA = dh.DEPOHIST_ID_MEGA)
				)
		    )
    )                               AS [ФИО сотрудника]
    ,dp.PERSON_MobPhone             AS [Вкладчик Телефон]
    ,dp.PERSON_ActAddress           AS [Вкладчик Адрес]
    ,dp.PERSON_ID_MINOR             AS PERSON_MINOR
    ,dp.PERSON_ID_MAJOR             AS PERSON_MAJOR
    ,dd.DEPOSIT_ID_MINOR            AS DEPOSIT_MINOR
    ,dd.DEPOSIT_ID_MAJOR            AS DEPOSIT_MAJOR
    ,dh.DEPOHIST_ID_MINOR           AS DEHIST_MINOR
    ,dh.DEPOHIST_ID_MAJOR           AS DEHIST_MAJOR
FROM DEPOSIT.DepoHist dh WITH (NOLOCK)
    INNER JOIN JBT_OperationAcc_ClerkOP06 o
        ON (o.ID_MEGA = dh.DEPOHIST_ID_MEGA)
            AND (o.DEPOSIT_MINOR = dh.DEPOHIST_DEPOSIT_MINOR)
            AND (o.DEPOSIT_MAJOR = dh.DEPOHIST_DEPOSIT_MAJOR)
            AND (o.OpDay = dh.DEPOHIST_OpDay)
            AND (o.BranchNo = dh.DEPOHIST_BranchNo)
            AND (o.OfficeNo = dh.DEPOHIST_Office)
            AND (o.OpNo = dh.DEPOHIST_JrnNo)
    INNER JOIN DEPOSIT.Deposit dd WITH (NOLOCK)
        ON (dd.DEPOSIT_ID_MEGA = dh.DEPOHIST_ID_MEGA)
            AND (dd.DEPOSIT_ID_MINOR = dh.DEPOHIST_DEPOSIT_MINOR)
            AND (dd.DEPOSIT_ID_MAJOR = dh.DEPOHIST_DEPOSIT_MAJOR)
    INNER JOIN CurrList cur
        ON (cur.Currency = dd.DEPOSIT_Currency)
    INNER JOIN CLIENT.Person dp WITH (NOLOCK)
        ON (dp.PERSON_ID_MEGA = dd.DEPOSIT_ID_MEGA)
            AND (dp.PERSON_ID_MINOR = dd.DEPOSIT_PERSON_MINOR)
            AND (dp.PERSON_ID_MAJOR = dd.DEPOSIT_PERSON_MAJOR)
    LEFT JOIN DEPOHEIR_ClerkOP06 dhh
        ON (dhh.DEPOSIT_MINOR = dd.DEPOSIT_ID_MINOR)
            AND (dhh.DEPOSIT_MAJOR = dd.DEPOSIT_ID_MAJOR)
            AND (dhh.ID_MEGA = dd.DEPOSIT_ID_MEGA)
            AND (dhh.No = dh.DEPOHIST_HeirNo)
    LEFT JOIN DEPOTRUSTED_ClerkOP06 dht
        ON (dht.DEPOSIT_MINOR = dd.DEPOSIT_ID_MINOR)
            AND (dht.DEPOSIT_MAJOR = dd.DEPOSIT_ID_MAJOR)
            AND (dht.ID_MEGA = dd.DEPOSIT_ID_MEGA)
            AND (dht.No = dh.DEPOHIST_HeirNo)
    LEFT JOIN DEPOINVESTOR_ClerkOP06 dhi
        ON (dhi.DEPOSIT_MINOR = dd.DEPOSIT_ID_MINOR)
            AND (dhi.DEPOSIT_MAJOR = dd.DEPOSIT_ID_MAJOR)
            AND (dhi.ID_MEGA = dd.DEPOSIT_ID_MEGA)
            AND (dhi.No = dh.DEPOHIST_HeirNo)
    LEFT JOIN Deposit.Deposit ddd WITH (NOLOCK)
        ON (ddd.DEPOSIT_PrintableNo = dh.DEPOHIST_DebitAccount)
    LEFT JOIN CLIENT.Person ddp WITH (NOLOCK)
        ON (ddp.PERSON_ID_MEGA = ddd.DEPOSIT_ID_MEGA)
            AND (ddp.PERSON_ID_MINOR = ddd.DEPOSIT_PERSON_MINOR)
            AND (ddp.PERSON_ID_MAJOR = ddd.DEPOSIT_PERSON_MAJOR)
    LEFT JOIN DEPOSIT.Deposit ddс WITH (NOLOCK)
        ON (ddс.DEPOSIT_PrintableNo = dh.DEPOHIST_CreditAccount)
    LEFT JOIN CLIENT.Person dсp WITH (NOLOCK)
        ON (dсp.PERSON_ID_MEGA = ddс.DEPOSIT_ID_MEGA)
            AND (dсp.PERSON_ID_MINOR = ddс.DEPOSIT_PERSON_MINOR)
            AND (dсp.PERSON_ID_MAJOR = ddс.DEPOSIT_PERSON_MAJOR)