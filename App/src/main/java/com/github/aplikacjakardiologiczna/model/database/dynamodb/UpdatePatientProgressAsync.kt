package com.github.aplikacjakardiologiczna.model.database.dynamodb

import android.app.Activity
import android.os.AsyncTask
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest


class UpdatePatientProgressAsync constructor(
    private val activity: Activity,
    private val nick: String,
    private val date: String,
    private val time: String,
    private val taskId: Int):
    AsyncTask<Void?, Void?, Void?>() {

    override fun doInBackground(vararg params: Void?): Void? {
        val credentialsProvider = CognitoCachingCredentialsProvider(
            activity.applicationContext,
            DynamoDBHelper.COGNITO_IDP_ID, DynamoDBHelper.COGNITO_IDP_REGION
        )

        val client = AmazonDynamoDBClient(credentialsProvider)
        client.setRegion(DynamoDBHelper.REGION)

        val itemKey = HashMap<String, AttributeValue>()
        itemKey["nick"] = AttributeValue().withS(nick)

        val attributeNames: MutableMap<String, String> = HashMap()
        attributeNames["#date"] = date
        attributeNames["#task_id"] = taskId.toString()

        val attributeValues: MutableMap<String, AttributeValue> = HashMap()
        attributeValues[":time_val"] = AttributeValue().withS(time)

        val request = UpdateItemRequest()
            .withTableName(DynamoDBHelper.TABLE_NAME)
            .withKey(itemKey)
            .withUpdateExpression("SET tasks.#date.#task_id = :time_val")
            .withExpressionAttributeValues(attributeValues)
            .withExpressionAttributeNames(attributeNames)

        client.updateItem(request)
        return null
    }
}