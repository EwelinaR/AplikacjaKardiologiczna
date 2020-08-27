package com.github.aplikacjakardiologiczna.model.database.aws

import android.app.Activity
import android.os.AsyncTask
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.mobileconnectors.dynamodbv2.document.Table
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient


/**
 * Async Task for handling the network retrieval of all the memos in DynamoDB
 */
class DatabaseAccessAsyncTask constructor(private val activity: Activity):
    AsyncTask<Void?, Void?, Document?>() {

    override fun doInBackground(vararg params: Void?): Document? {
        val credentialsProvider = CognitoCachingCredentialsProvider(activity.applicationContext,
            DynamoDBHelper.COGNITO_IDP_ID, DynamoDBHelper.COGNITO_IDP_REGION)

        val client: AmazonDynamoDB = AmazonDynamoDBClient(credentialsProvider)
        client.setRegion(DynamoDBHelper.REGION)

        val dbTable = Table.loadTable(client, DynamoDBHelper.TABLE_NAME)

        return dbTable.getItem(
            Primitive("ER1234")
        )
    }
}