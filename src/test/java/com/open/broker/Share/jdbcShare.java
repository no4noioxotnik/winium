package com.open.broker.Share;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class jdbcShare {
    public String host;
    public int port;
    public String jdbcConnection;
    public String dbname;
    public String username;
    public String password;
    public String connectionUrl;
    public ResultSet resultSet;
    public String sql;
    public int field2;
    public String field;
    public String name;
    public String nTableName;
    public String columnName;
    public String secondColumnName;
    public Connection connection = null;
    public ResultSetMetaData meta;
    public String schema;
    public ArrayList<String> cols;
    public ArrayList<String> cols2;
    public int i;


    //    70-е отложенные проводки с незаполненной Аналитикой1
    public String withOutAnalitic1 = "select analytic_1, * from planned_transactions where account=70 and status_id=3 and analytic_1 is null and services_date >='20170101' order by 1 desc";

    //    Проверка созданных отложенных по ИИС
    public String potpTransactIis = "select * from planned_transactions pt inner join client cl on pt.source_account_id = cl.client_id inner join servicesopen SO on cl.client_id=so.clientid  where cl.client_base_id in   (   select   r.client_id    from opentaxes.dbo.taxes_save_running_total_iis_registry_total r   where r.iis_client_id in   (   select   cl.client_id   from client cl    inner join servicesopen SO on SO.clientid = cl.client_id    inner join dogovor d on d.client_id = SO.clientid    where    SO.serviceid = 626 and  SO.end_date is not null and d.dogovor_type = 643 and   d.date_end >='20170101'  )  and r.calc_year = 0  and taxes_nob_item_id = 73  )  and so.serviceid = 623 and pt.account = 73  and pt.creation_date >='20171110'  and pt.comment like '%i>%'";

    //    Проверка созданных отложенных по ИИС(не переведенных)
    public String potpTransactIisNotTranfered = "select * from planned_transactions pt inner join client cl on pt.source_account_id = cl.client_id inner join servicesopen SO on cl.client_id=so.clientid where cl.client_base_id in (select r.client_id from opentaxes.dbo.taxes_save_running_total_iis_registry_total r where r.iis_client_id in (select cl.client_id from client cl inner join servicesopen SO  on SO.clientid = cl.client_id inner join dogovor d on d.client_id = SO.clientid where SO.serviceid <> 626 and SO.end_date is not null and d.dogovor_type = 643 and d.date_end >='20170101' ) and r.calc_year = 0 ) and so.serviceid = 623 and pt.account = 73 and pt.creation_date >='20171110' and pt.comment like '%i>%'";

    //    Проверка отложенных проводок по коду клиента
    public String potpTransactClientId = "select * from planned_transactions p where ((p.creation_date >= '20160101' and p.services_date < '20170101' and p.account = 70 and p.source_account_id in (select c.client_id from client c where c.client_base_id = (select c1.client_base_id from client c1 where c1.code = '91987')) ) or (p.creation_date >= '20170101' and p.account = 73 and p.source_account_id in (select c.client_id from client c where c.client_base_id = (select c1.client_base_id from client c1 where c1.code = '91987')) )) and p.status_id <> 4 order by p.owner_id, p.dateins, p.id";


}
