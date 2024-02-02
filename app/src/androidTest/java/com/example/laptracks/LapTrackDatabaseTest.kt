package com.example.laptracks

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.laptracks.data.AppDatabase
import com.example.laptracks.data.Student
import com.example.laptracks.data.StudentDao
import com.example.laptracks.data.Workout
import com.example.laptracks.data.WorkoutDao

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class LapTrackDatabaseTest {
    private lateinit var studentDao: StudentDao
    private lateinit var workoutDao: WorkoutDao
    private lateinit var db: AppDatabase

    private val student =
        Student(id = 1, firstName = "Billy", lastName = "Smith", displayName = "BSmith")
    private val student2 =
        Student(id = 2, firstName = "Susan", lastName = "Williams", displayName = "SWilliams")

    private val workout1 =
        Workout(1, date = "Date1", listOf(5_000L, 6_000L), interval = "300", studentId = 1)
    private val workout2 =
        Workout(2, date = "Date1", listOf(5_000L, 6_000L), interval = "300", studentId = 1)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).allowMainThreadQueries().build()

        studentDao = db.studentDao()
        workoutDao = db.workoutDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private suspend fun createStudent() {
        studentDao.insert(student)
        studentDao.insert(student2)
    }

    private fun createWorkouts() {
        workoutDao.workoutInserts(workout1)
        workoutDao.workoutInserts(workout2)
    }

    @Test
    @Throws(IOException::class)
    fun daoGetAllStudent() = runBlocking {
        createStudent()

        val result = studentDao.getAllStudents().first()
        assertEquals(result[0], student)
    }

    @Test
    @Throws(IOException::class)
    fun getGetStudentsWithWorkout() = runBlocking {
        createStudent()
        createWorkouts()
        val result = studentDao.loadStudentAndWorkouts(student.id).first()
        assertEquals(result.keys.first(), student)
        assertEquals(result.values.first(), listOf(workout1, workout2))
    }

    @Test
    @Throws(IOException::class)
    fun getStudentsWithWorkouts() = runBlocking {
        createStudent()
        createWorkouts()

        val result = studentDao.getAllStudentsWithWorkouts().first()
        assertEquals(result.keys.size, 2)
    }

    @Test
    @Throws(IOException::class)
    fun updateStudentInformation() = runBlocking {
        createStudent()

        studentDao.update(
            Student(
                id = 1,
                firstName = "Little Bill",
                lastName = "Costco",
                displayName = "LBCostco"
            )
        )
        val result = studentDao.getStudent(1)
        assertEquals(
            Student(
                id = 1,
                firstName = "Little Bill",
                lastName = "Costco",
                displayName = "LBCostco"
            ), result.first()
        )
    }

    @Test
    @Throws(IOException::class)
    fun deleteStudents() = runBlocking {
        createStudent()

        studentDao.delete(student)
        studentDao.delete(student2)

        val result = studentDao.getAllStudents().first()
        assertTrue(result.isEmpty())
    }

    @Test
    @Throws(IOException::class)
    fun createsWorkout() = runBlocking {
        createStudent()
        createWorkouts()

        val result = workoutDao.getWorkouts().first()
        assertEquals(2, result?.size)
    }
}