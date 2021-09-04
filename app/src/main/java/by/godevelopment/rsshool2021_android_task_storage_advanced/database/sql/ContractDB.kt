package by.godevelopment.rsshool2021_android_task_storage_advanced.database.sql

// A good way to organize a contract class is to put definitions that are global to your whole database in the root level of the class.
// Then create an inner class for each table. Each inner class enumerates the corresponding table's columns.
object ContractDB {
    // Table contents are grouped together in an anonymous object.

    // Реализуя BaseColumns интерфейс, ваш внутренний класс может унаследовать поле первичного ключа,
    // называемое _ID , наличие которых будут ожидать некоторые Android классы, такие как курсоры адаптеров.
    // Это не обязательно, но это может помочь вашей работе с базой данных в гармонии с библиотеками Android.
    object FeedEntry {
        const val TABLE_NAME = "cat_table"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_AGE = "age"
        const val COLUMN_NAME_BREED = "breed"

        // Raw query for db:

        // DELETE FROM cat_table WHERE id = 1;
        // INSERT INTO cat_table (name, age, breed) VALUES ("Jesus", 33, "Heaven");
        // UPDATE cat_table SET name = 'Pluto' WHERE ID = 33;
    }
}