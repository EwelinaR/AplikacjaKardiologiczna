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

class DatabaseAccess constructor(private val context: Context) {

    fun readUserInfo(date: String, nick: String): Document? {
        val client = getDatabaseClient()
        val dbTable = Table.loadTable(client, DynamoDBHelper.USER_TABLE_NAME)

        return dbTable.getItem(
            Primitive(nick),
            Primitive(date)
        )
    }

    fun writeTimeOfTask(taskId: Int, date: String, time: String, nick: String) {
        val client = getDatabaseClient()

        val itemKey = HashMap<String, AttributeValue>()
        itemKey["nick"] = AttributeValue().withS(nick)
        itemKey["date"] = AttributeValue().withS(date)

        val attributeNames: MutableMap<String, String> = HashMap()
        attributeNames["#time"] = "time"

        val attributeValues: MutableMap<String, AttributeValue> = HashMap()
        attributeValues[":time_val"] = AttributeValue().withS(time)

        val request = UpdateItemRequest()
            .withTableName(DynamoDBHelper.USER_TABLE_NAME)
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