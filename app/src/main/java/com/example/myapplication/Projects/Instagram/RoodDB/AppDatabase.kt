package com.example.myapplication.Projects.Instagram.RoodDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.Projects.Instagram.RoodDB.Comment.Comment
import com.example.myapplication.Projects.Instagram.RoodDB.Comment.CommentDao
import com.example.myapplication.Projects.Instagram.RoodDB.Post.Post
import com.example.myapplication.Projects.Instagram.RoodDB.Post.PostDao
import com.example.myapplication.Projects.Instagram.RoodDB.User.User
import com.example.myapplication.Projects.Instagram.RoodDB.User.UserDao

@Database(entities = [User::class, Post::class, Comment::class], version = 6)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "instagram_database"
                )
                    .addMigrations(MIGRATION_5_6)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}