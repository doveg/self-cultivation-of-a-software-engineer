@Data
class Task {
    private Long taskId;
    // 初始化为初始态
    private State state = new TaskInit();

    // 更新状态
    public void updateState(ActionType actionType) {
        state.update(this, actionType);
    }
}


// 任务状态抽象接口
interface State {
    // 默认实现，不做任何处理
    default void update(Task task, ActionType actionType) {
        // do nothing
    }
}


// 任务暂停状态
class TaskPaused implements State {
    @Override
    public void update(Task task, ActionType actionType) {
        if (actionType == ActionType.START) {
            task.setState(new TaskOngoing());
        } else if (actionType == ActionType.EXPIRE) {
            task.setState(new TaskExpired());
        }
    }
}


// 任务完成状态
class TaskFinished implements State {

}


// 任务过期状态
class TaskExpired implements State {

}


// 抽象观察者
interface Observer {
    void response(Long taskId); // 反应
}


// 抽象目标
abstract class Subject {
    protected List<Observer> observers = new ArrayList<Observer>();

    // 增加观察者方法
    public void add(Observer observer) {
        observers.add(observer);
    }

    // 删除观察者方法
    public void remove(Observer observer) {
        observers.remove(observer);
    }

    // 通知观察者方法
    public void notifyObserver(Long taskId) {
        for (Observer observer : observers) {
            observer.response(taskId);
        }
    }
}


// 活动观察者
class ActivityObserver implements Observer {
    private ActivityService activityService;

    @Override
    public void response(Long taskId) {
        activityService.notifyFinished(taskId);
    }
}


// 任务管理观察者
class TaskManageObserver implements Observer {
    private TaskManager taskManager;

    @Override
    public void response(Long taskId) {
        taskManager.release(taskId);
    }
}


// 任务进行状态
class TaskOngoing extends Subject implements State {
    @Override
    public void update(Task task, ActionType actionType) {
        if (actionType == ActionType.ACHIEVE) {
            task.setState(new TaskFinished());
            // 通知
            notifyObserver(task.getTaskId());
        } else if (actionType == ActionType.STOP) {
            task.setState(new TaskPaused());
        } else if (actionType == ActionType.EXPIRE) {
            task.setState(new TaskExpired());
        }
    }
}


// 任务初始状态
class TaskInit implements State {
    @Override
    public void update(Task task, ActionType actionType) {
        if (actionType == ActionType.START) {
            TaskOngoing taskOngoing = new TaskOngoing();
            taskOngoing.add(new ActivityObserver());
            taskOngoing.add(new TaskManageObserver());
            task.setState(taskOngoing);
        }
    }
}
