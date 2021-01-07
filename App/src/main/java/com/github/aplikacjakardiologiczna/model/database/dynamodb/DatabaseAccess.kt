package com.github.aplikacjakardiologiczna.model.database.dynamodb

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.ScanRequest
import com.amazonaws.services.dynamodbv2.model.ScanResult
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest
import com.github.aplikacjakardiologiczna.model.database.entity.NextId
import com.google.gson.Gson

class DatabaseAccess constructor(private val context: Context) {

    fun getHistoryNextId(): Long {
        val client = getDatabaseClient()
        val dbTable = Table.loadTable(client, DynamoDBHelper.HISTORY_TABLE_NAME)

        val docId  = dbTable.getItem(
                Primitive(0),
        )
        val nextId = Gson().fromJson(Document.toJson(docId), NextId::class.java)

        val itemKey = HashMap<String, AttributeValue>()
        itemKey["id"] = AttributeValue().withN("0")

        val attributeNames: MutableMap<String, String> = HashMap()
        attributeNames["#nextId"] = "nextId"

        val attributeValues: MutableMap<String, AttributeValue> = HashMap()
        attributeValues[":nextId_val"] = AttributeValue().withN((nextId.nextId + 1).toString())

        val request = UpdateItemRequest()
                .withTableName(DynamoDBHelper.HISTORY_TABLE_NAME)
                .withKey(itemKey)
                .withUpdateExpression("SET #nextId = :nextId_val")
                .withExpressionAttributeValues(attributeValues)
                .withExpressionAttributeNames(attributeNames)

        client.updateItem(request)
        return nextId.nextId
    }

    fun readUserTaskId(date: String, nick: String): ScanResult {
        val client = getDatabaseClient()

        val attributeNames: MutableMap<String, String> = HashMap()
        attributeNames["#date"] = "date"
        attributeNames["#nick"] = "nick"

        val attributeValues: MutableMap<String, AttributeValue> = HashMap()
        attributeValues[":date_val"] = AttributeValue().withS(date)
        attributeValues[":nick_val"] = AttributeValue().withS(nick)

        val scanRequest = ScanRequest()
                .withTableName(DynamoDBHelper.HISTORY_TABLE_NAME)
                .withFilterExpression("#date = :date_val AND #nick = :nick_val")
                .withExpressionAttributeValues(attributeValues)
                .withExpressionAttributeNames(attributeNames)

        return client.scan(scanRequest)
    }

    fun readUserInfo(id: String): Document? {
        val client = getDatabaseClient()
        val dbTable = Table.loadTable(client, DynamoDBHelper.HISTORY_TABLE_NAME)

        return dbTable.getItem(
                Primitive(id.toLong())
        )
    }

    fun writeTimeOfTask(taskId: Int, time: String, id: String) {
        val client = getDatabaseClient()

        val itemKey = HashMap<String, AttributeValue>()
        itemKey["id"] = AttributeValue().withN(id)

        val attributeNames: MutableMap<String, String> = HashMap()
        attributeNames["#time"] = "time"

        val attributeValues: MutableMap<String, AttributeValue> = HashMap()
        attributeValues[":time_val"] = AttributeValue().withS(time)

        val request = UpdateItemRequest()
            .withTableName(DynamoDBHelper.HISTORY_TABLE_NAME)
            .withKey(itemKey)
            .withUpdateExpression("SET userTasks[${taskId}].#time = :time_val")
            .withExpressionAttributeValues(attributeValues)
            .withExpressionAttributeNames(attributeNames)

        client.updateItem(request)
    }

    fun readTaskFromDatabase(group: String, id: Int): Document? {
        val client = getDatabaseClient()
        val dbTable = Table.loadTable(client, DynamoDBHelper.TASK_TABLE_NAME)

        return dbTable.getItem(
            Primitive(group),
            Primitive(id)
        )
    }

    fun readUserFromDatabase(username: String): ScanResult {
        val client = getDatabaseClient()

        val attributeValues: MutableMap<String, AttributeValue> = HashMap()
        attributeValues[":val"] = AttributeValue().withS(username)

        val scanRequest = ScanRequest()
            .withTableName(DynamoDBHelper.HISTORY_TABLE_NAME)
            .withFilterExpression("nick = :val")
            .withExpressionAttributeValues(attributeValues)

        return client.scan(scanRequest)
    }

    fun readTasksFromDatabase(group: String): ScanResult {
        val client = getDatabaseClient()

        val attributeNames: MutableMap<String, String> = HashMap()
        attributeNames["#group"] = "group"

        val attributeValues: MutableMap<String, AttributeValue> = HashMap()
        attributeValues[":val"] = AttributeValue().withS(group)

        val scanRequest = ScanRequest()
            .withTableName(DynamoDBHelper.TASK_TABLE_NAME)
            .withFilterExpression("#group = :val")
            .withExpressionAttributeValues(attributeValues)
            .withExpressionAttributeNames(attributeNames)

        return client.scan(scanRequest)
    }

    fun addUserInfo(userInfo: String) {
        val client = getDatabaseClient()
        val dbTable = Table.loadTable(client, DynamoDBHelper.HISTORY_TABLE_NAME)

        dbTable.putItem(Document.fromJson(userInfo))
    }

    private fun getDatabaseClient(): AmazonDynamoDB {
        val credentialsProvider = CognitoCachingCredentialsProvider(
            context,
            DynamoDBHelper.COGNITO_IDP_ID, DynamoDBHelper.COGNITO_IDP_REGION
        )

        val client: AmazonDynamoDB = AmazonDynamoDBClient(credentialsProvider)
        client.setRegion(DynamoDBHelper.REGION)

        return client
    }
}
