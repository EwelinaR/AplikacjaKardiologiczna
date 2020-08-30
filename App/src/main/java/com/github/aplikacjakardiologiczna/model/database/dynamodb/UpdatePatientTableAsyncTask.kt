package com.github.aplikacjakardiologiczna.model.database.dynamodb

import android.app.Activity
import android.os.AsyncTask
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.model.AttributeAction
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest


class UpdatePatientTableAsyncTask constructor(private val activity: Activity):
    AsyncTask<Void?, Void?, Void?>() {

    override fun doInBackground(vararg params: Void?): Void? {
        val credentialsProvider = CognitoCachingCredentialsProvider(
            activity.applicationContext,
            DynamoDBHelper.COGNITO_IDP_ID, DynamoDBHelper.COGNITO_IDP_REGION
        )

        val client = AmazonDynamoDBClient(credentialsProvider)
        client.setRegion(DynamoDBHelper.REGION)

        val itemKey = HashMap<String, AttributeValue>()
        itemKey["nick"] = AttributeValue().withS("ER1234")

        val updatedValues = HashMap<String, AttributeValueUpdate>()
        // Update the column specified by name with updatedVal
        updatedValues["group"] = AttributeValueUpdate()
            .withValue(AttributeValue().withS("A"))
            .withAction(AttributeAction.PUT)

        val request = UpdateItemRequest()
            .withTableName(DynamoDBHelper.TABLE_NAME)
            .withKey(itemKey)
            .withAttributeUpdates(updatedValues)

        client.updateItem(request)

        return null
    }
}