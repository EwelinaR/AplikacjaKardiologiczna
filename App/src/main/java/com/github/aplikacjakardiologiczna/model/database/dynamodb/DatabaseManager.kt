package com.github.aplikacjakardiologiczna.model.database.dynamodb

import android.content.Context
import android.util.Log
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest
import com.google.gson.Gson
import org.json.JSONObject
import java.text.DateFormat
import java.util.Date


class DatabaseManager constructor(private val context: Context) {

    // temporary using fake name
    private val NICK = "ER1234"

    fun getTodaysDate(): String {
        return "05-09-2020" // for tests
//        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
//        return dateFormat.format(Date())
    }

    fun getTodaysTime(): String {
        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)
        return timeFormat.format(Date())
    }

    fun updateTask(taskId: Int) {
        val credentialsProvider = CognitoCachingCredentialsProvider(
            context,
            DynamoDBHelper.COGNITO_IDP_ID, DynamoDBHelper.COGNITO_IDP_REGION
        )

        val client = AmazonDynamoDBClient(credentialsProvider)
        client.setRegion(DynamoDBHelper.REGION)

        val itemKey = HashMap<String, AttributeValue>()
        itemKey["nick"] = AttributeValue().withS("TEST")
        itemKey["date"] = AttributeValue().withS(getTodaysDate())

        val attributeNames: MutableMap<String, String> = HashMap()
        attributeNames["#time"] = "time"

        val attributeValues: MutableMap<String, AttributeValue> = HashMap()
        attributeValues[":time_val"] = AttributeValue().withS(getTodaysTime())

        val request = UpdateItemRequest()
            .withTableName("User")
            .withKey(itemKey)
            .withUpdateExpression("SET userTasks[${taskId}].#time = :time_val")
            .withExpressionAttributeValues(attributeValues)
            .withExpressionAttributeNames(attributeNames)

        client.updateItem(request)
    }

    fun markTaskAsCompleted(taskId: Int) {
        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
        val timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT)

        val date = dateFormat.format(Date())
        val time = timeFormat.format(Date())

       // val task = UpdatePatientProgressAsync(activity, NICK, date, time, taskId)
        updateTask(taskId)
        Log.e("DB", "Completed task: $taskId")


      //  task.execute()
    }

    fun createTasks(taskIds: List<Int>, isForToday: Boolean = false) {

    }

    fun doInBackground(): Document? {
        val credentialsProvider = CognitoCachingCredentialsProvider(context,
            DynamoDBHelper.COGNITO_IDP_ID, DynamoDBHelper.COGNITO_IDP_REGION)

        val client: AmazonDynamoDB = AmazonDynamoDBClient(credentialsProvider)
        client.setRegion(DynamoDBHelper.REGION)

        val dbTable = Table.loadTable(client, "User")


        /*
        // search for more items
        var expression = Expression()
        expression.setExpressionStatement("Tag = :tag")
        expression.withExpressionAttibuteValues(":tag", Primitive("sports"))

        var searchResult = dbTable.query(Primitive("myUser"), expression)
        var documents = searchResult.getAllResults()
        */


        return dbTable.getItem(//"nick", "TEST", "date", "05-09-2020")
            Primitive("TEST"),
        Primitive("05-09-2020")
        )
    }

    fun getTasksForToday(): UserInfo {
        val doc = doInBackground()
        val date = getTodaysDate()
        val taskIds = ArrayList<Int>()

        val jsonObject = JSONObject(doc.toString())

        val user =  Gson().fromJson(Document.toJson(doc),  UserInfo::class.java)
        Log.e("DB", user.toString())
        return user

    }


    fun doInBackground2(group: String, id: Int): Document? {
        val credentialsProvider = CognitoCachingCredentialsProvider(context,
            DynamoDBHelper.COGNITO_IDP_ID, DynamoDBHelper.COGNITO_IDP_REGION)

        val client: AmazonDynamoDB = AmazonDynamoDBClient(credentialsProvider)
        client.setRegion(DynamoDBHelper.REGION)

        val dbTable = Table.loadTable(client, "Task")


        /*
        // search for more items
        var expression = Expression()
        expression.setExpressionStatement("Tag = :tag")
        expression.withExpressionAttibuteValues(":tag", Primitive("sports"))

        var searchResult = dbTable.query(Primitive("myUser"), expression)
        var documents = searchResult.getAllResults()
        */



        return dbTable.getItem(
            Primitive(group),
            Primitive(id)
        )
    }

    fun getTaskDescription(group: String, ids: List<Int>): List<TaskDetails> {
        val tasks = ArrayList<TaskDetails>()

        for(id in ids) {
            val doc = doInBackground2(group, id)
            tasks.add(Gson().fromJson(Document.toJson(doc), TaskDetails::class.java))

        }
        Log.e("DB", tasks.toString())

        return tasks
    }

    fun setGroup(group: String) {
       // UpdateSingleValueAsync(activity, NICK, "group", group)
    }

}