package com.github.aplikacjakardiologiczna.model.database.aws

import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions

object DynamoDBHelper {
    const val COGNITO_IDP_ID = "eu-central-1:c80fd971-ffbd-4dba-9f99-0efd3a90c2a9"
    val COGNITO_IDP_REGION = Regions.EU_CENTRAL_1  //allotted region
    val REGION = Region.getRegion(COGNITO_IDP_REGION)
    const val TABLE_NAME = "patient"
}
