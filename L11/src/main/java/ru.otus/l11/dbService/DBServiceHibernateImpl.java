package ru.otus.l11.dbService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.l11.base.DBService;
import ru.otus.l11.base.dataSets.AddressDataSet;
import ru.otus.l11.base.dataSets.DataSet;
import ru.otus.l11.base.dataSets.PhoneDataSet;
import ru.otus.l11.base.dataSets.UserDataSet;
import ru.otus.l11.dbService.dao.UserDAO;
import ru.otus.l11.dbService.dao.UserHibernateDAO;

import java.sql.SQLException;

public class DBServiceHibernateImpl implements DBService {
    private SessionFactory sessionFactory;

    public DBServiceHibernateImpl() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://db4free.net:3306/otuslessions");
        configuration.setProperty("hibernate.connection.username", "otusstudent");
        configuration.setProperty("hibernate.connection.password", "Fktrctq161077");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        sessionFactory = createSessionFactory(configuration);

    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry service = builder.build();
        return configuration.buildSessionFactory(service);
    }

    @Override
    public void dropTable(Class<? extends DataSet> dataSetClass) throws SQLException {

    }

    @Override
    public <T extends DataSet> void save(T user) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            UserDAO dao = new UserHibernateDAO(session);
            dao.save(user);
        }

    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> cls) throws SQLException {
        try (Session session = sessionFactory.openSession()) {
            UserDAO dao = new UserHibernateDAO(session);
            return dao.load(id, cls);
        }
    }
}
