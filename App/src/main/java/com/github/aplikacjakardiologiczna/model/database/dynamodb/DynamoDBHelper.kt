package com.github.aplikacjakardiologiczna.model.database.dynamodb

import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions

object DynamoDBHelper {
    const val COGNITO_IDP_ID = "eu-west-2:f59984be-b08e-4a82-ba7f-013bc6972252"
    val COGNITO_IDP_REGION = Regions.EU_WEST_2
    val REGION = Region.getRegion(COGNITO_IDP_REGION)
    const val TASK_TABLE_NAME = "Task"
    const val HISTORY_TABLE_NAME = "History"
}
