package database.com.timediffproject;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.timediffproject.database.AlarmModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig alarmModelDaoConfig;

    private final AlarmModelDao alarmModelDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        alarmModelDaoConfig = daoConfigMap.get(AlarmModelDao.class).clone();
        alarmModelDaoConfig.initIdentityScope(type);

        alarmModelDao = new AlarmModelDao(alarmModelDaoConfig, this);

        registerDao(AlarmModel.class, alarmModelDao);
    }
    
    public void clear() {
        alarmModelDaoConfig.clearIdentityScope();
    }

    public AlarmModelDao getAlarmModelDao() {
        return alarmModelDao;
    }

}
