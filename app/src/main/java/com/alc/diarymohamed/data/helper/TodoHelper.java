package com.alc.diarymohamed.data.helper;

import com.student.android.data.model.PreparationModel;
import com.student.android.data.model.TodoModel;
import com.student.android.shared.Globals;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;


public class TodoHelper {

    private static final Logger SLF_LOGGER = LoggerFactory.getLogger(TodoHelper.class);
    private Realm mRealm;

    public TodoHelper() {
        mRealm = Globals.realmMigration();
    }

    public void addTodo(String idTodo, String titleTodo, String categoryTodo, Date dateTodo, int timeTodo,
                        int durationTodo, String priorityTodo, String descriptionTodo, Boolean doneTodo,
                        Boolean selected, Boolean preparation, Boolean remind, Boolean priorityImportant) {

        TodoModel todoModel = new TodoModel();

        try {
            mRealm.beginTransaction();
            todoModel = mRealm.createObject(TodoModel.class);
            todoModel.setIdTodo(idTodo);
            todoModel.setTitleTodo(titleTodo);
            todoModel.setCategoryTodo(categoryTodo);
            todoModel.setDateTodo(dateTodo);
            todoModel.setTimeTodo(timeTodo);
            todoModel.setDurationTodo(durationTodo);
            todoModel.setPriorityTodo(priorityTodo);
            todoModel.setDescriptionTodo(descriptionTodo);
            todoModel.setDoneTodo(doneTodo);
            todoModel.setSelected(selected);
            todoModel.setPreparation(preparation);
            todoModel.setRemindTodo(remind);
            todoModel.setPriorityImportant(priorityImportant);


        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.copyToRealm(todoModel);
            mRealm.commitTransaction();
            //realm.close();
        }
    }

    public TodoModel findTodo(String idTodo) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", idTodo)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                SLF_LOGGER.info("todoModel.find returns " + todoModel.getTitleTodo());
            }
            //realm.close();
        }
        return todoModel;
    }


    public RealmResults<TodoModel> findDoneTodo(Boolean doneTodo) {
        RealmResults<TodoModel> todoModels = null;
        try {
            mRealm.beginTransaction();
            //String []fieldNames={"dateTodo","priorityTodo"};
            String[] fieldNames = {"priorityTodo", "dateTodo"};
            Sort sort[] = {Sort.ASCENDING, Sort.ASCENDING};
            todoModels = mRealm.where(TodoModel.class)
                    .equalTo("doneTodo", doneTodo)
                    .findAllSorted(fieldNames, sort);
            //.findAll();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModels == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            }
            //realm.close();
        }
        return todoModels;
    }

    public RealmResults<TodoModel> findDoneSortedTodo(Boolean doneTodo, String[] fieldNames) {
        RealmResults<TodoModel> todoModels = null;
        try {
            mRealm.beginTransaction();
            Sort sort[] = {Sort.ASCENDING, Sort.ASCENDING};
            todoModels = mRealm.where(TodoModel.class)
                    .equalTo("doneTodo", doneTodo)
                    .findAllSorted(fieldNames, sort);
            //.findAll();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModels == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            }
            //realm.close();
        }
        return todoModels;
    }

    public RealmResults<TodoModel> findDoneSortedTodo(Boolean doneTodo, String[] fieldNames, String titleTodo) {
        RealmResults<TodoModel> todoModels = null;
        try {
            mRealm.beginTransaction();
            Sort sort[] = {Sort.ASCENDING, Sort.ASCENDING};
            todoModels = mRealm.where(TodoModel.class)
                    .equalTo("doneTodo", doneTodo)
                    .contains("titleTodo", titleTodo)
                    .findAllSorted(fieldNames, sort);
            //.findAll();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModels == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            }
            //realm.close();
        }
        return todoModels;
    }

    public RealmResults<TodoModel> findAllSelectedTodo() {
        RealmResults<TodoModel> todoModels = null;
        try {
            mRealm.beginTransaction();
            todoModels = mRealm.where(TodoModel.class)
                    .equalTo("selected", true)
                    .findAll();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModels == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            }
            //realm.close();
        }
        return todoModels;
    }

    public void updateTodo(String id, String titleTodo, String categoryTodo, Date dateTodo, int timeTodo,
                           int durationTodo, String priorityTodo, String descriptionTodo, Boolean doneTodo,
                           Boolean selected, Boolean preparation, Boolean priorityImportant) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setIdTodo(id);
                    todoModel.setTitleTodo(titleTodo);
                    todoModel.setCategoryTodo(categoryTodo);
                    todoModel.setDateTodo(dateTodo);
                    todoModel.setTimeTodo(timeTodo);
                    todoModel.setDurationTodo(durationTodo);
                    todoModel.setPriorityTodo(priorityTodo);
                    todoModel.setDescriptionTodo(descriptionTodo);
                    todoModel.setDoneTodo(doneTodo);
                    todoModel.setSelected(selected);
                    todoModel.setPreparation(preparation);
                    todoModel.setPriorityImportant(priorityImportant);


                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }
    public void updateToDoEndDate(String id, int dateEndTodo) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setIdTodo(id);
                    todoModel.setDateEndTodo(dateEndTodo);


                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }

    public void prepareToAddNewPreparation(String id, String titleTodo, String categoryTodo,
                                           String priorityTodo, String descriptionTodo, Boolean preparation) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setIdTodo(id);
                    todoModel.setTitleTodo(titleTodo);
                    todoModel.setCategoryTodo(categoryTodo);
                    todoModel.setPriorityTodo(priorityTodo);
                    todoModel.setDescriptionTodo(descriptionTodo);
                    todoModel.setPreparation(preparation);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }

    public void prepareToAddNewTaskDate(String id, String titleTodo, String categoryTodo,
                                        String priorityTodo, String descriptionTodo, Date dateTodo, int dateEndTodo) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setIdTodo(id);
                    todoModel.setTitleTodo(titleTodo);
                    todoModel.setCategoryTodo(categoryTodo);
                    todoModel.setPriorityTodo(priorityTodo);
                    todoModel.setDescriptionTodo(descriptionTodo);
                    todoModel.setDateTodo(dateTodo);
                    todoModel.setDateEndTodo(dateEndTodo);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }

    public void updatePriorityTodo(String id, String priorityTodo) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setPriorityTodo(priorityTodo);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }
    public void makeItImportant(String id, Boolean priorityImportant) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setPriorityImportant(priorityImportant);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }
    public void makeItUnImportant(String id, Boolean priorityImportant) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setPriorityImportant(priorityImportant);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }
    public void updatePriorityDateTodo(String id, String priorityTodo, Date dateSelected) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setPriorityTodo(priorityTodo);
                    todoModel.setDateTodo(dateSelected);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }

    public void updateTodoDone(String id, Boolean doneTodo) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setDoneTodo(doneTodo);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }

    public void updatePreparation(String id, Boolean preparation) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setPreparation(preparation);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }

    public void updateSelected(String id, Boolean selected) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setSelected(selected);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }

    public void updateRemind(String id, Boolean remind) {

        if (null == mRealm) {
            mRealm = Globals.realmMigration();
        }

        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            try {
                mRealm.commitTransaction();
                if (todoModel == null) {
                    SLF_LOGGER.info("todoModel.find returns null");
                } else {
                    try {
                        mRealm.beginTransaction();
                        todoModel.setRemindTodo(remind);
                        SLF_LOGGER.info("setRemindTodo(remind) to " + remind);
                        mRealm.commitTransaction();
                    } catch (Exception e) {
                        SLF_LOGGER.error("error : " + e);
                    }
                }
            }catch (Exception e) {
                SLF_LOGGER.error("error ", e);
            }
                //realm.close();
        }
    }


    public void updateTaskDateTime(String id, int totalTaskTimeMinute, int totalTaskDurationMinute) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setTimeTodo(totalTaskTimeMinute);
                    todoModel.setDurationTodo(totalTaskDurationMinute);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }

    public void updateTaskDateTime(String id, int totalTaskTimeMinute, int totalTaskDurationMinute, Date dateTask) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setTimeTodo(totalTaskTimeMinute);
                    todoModel.setDurationTodo(totalTaskDurationMinute);
                    todoModel.setDateTodo(dateTask);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }


    public void updateTaskWithPreparation(String id, PreparationModel preparationModel, int durationTodo) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", id)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                try {
                    mRealm.beginTransaction();
                    todoModel.setPreparation(true);
                    todoModel.getListPreparation().add(preparationModel);
                    todoModel.setDurationTodo(durationTodo);
                    mRealm.commitTransaction();
                } catch (Exception e) {
                    SLF_LOGGER.error("error : " + e);
                }
            }
            //realm.close();
        }
    }


    public void clearPreparation(String idTodo) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", idTodo)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                SLF_LOGGER.info("todoModel.find returns " + todoModel.getTitleTodo());
                mRealm.beginTransaction();
                RealmList<PreparationModel> listPreparation = new RealmList<>();
                todoModel.setListPreparation(listPreparation);
                mRealm.commitTransaction();
            }
            //realm.close();
        }
    }

    public void removeTodo(String idTodo) {
        TodoModel todoModel = null;
        try {
            mRealm.beginTransaction();
            todoModel = mRealm.where(TodoModel.class)
                    .equalTo("idTodo", idTodo)
                    .findFirst();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        } finally {
            mRealm.commitTransaction();
            if (todoModel == null) {
                SLF_LOGGER.info("todoModel.find returns null");
            } else {
                SLF_LOGGER.info("todoModel.find returns " + todoModel.getTitleTodo());
                mRealm.beginTransaction();
                todoModel.removeFromRealm();
                mRealm.commitTransaction();
            }
            //realm.close();
        }
    }

    public void removeTodo(List<TodoModel> selectedTodoArray) {

        List<TodoModel> list = new ArrayList<>();
        list.addAll(selectedTodoArray);

        try {
            mRealm.beginTransaction();
            for (TodoModel myModel : list) {
                myModel.removeFromRealm();
            }
            mRealm.commitTransaction();
        } catch (Exception e) {
            mRealm.cancelTransaction();
        }
    }

    public void removeAllTodo() {
        SLF_LOGGER.info("deleteAllTodo()");
        try {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.clear(TodoModel.class);
                }
            });
        } catch (Exception e) {
            SLF_LOGGER.error("error: " + e);
        }
    }

}