package org.tawakal.composemphelloworld.utils

object Constants {
    const val DATASTORE_PREF_FILENAME = "prefs.preferences_pb"
    const val DATASTORE_PREF_MSAL_ACCESSTOKEN_KEY = "msal_access_token"
    const val DATASTORE_PREF_AZURE_ACCESSTOKEN_KEY = "azure_app_config_access_token"
    const val DATASTORE_PREF_CUSTOMERAPI_KEY = "customer_api"
    const val DATASTORE_PREF_GOOGLE_ADDRESS_API_KEY ="google_address_api_key"
    const val DATASTORE_PREF_MSAL_SUB_KEY ="msal_sender_object_id"
    const val DATASTORE_PREF_USER_CURRENCYCODE_KEY ="currency_code"

    const val AZURE_APP_CONFIG_ACCESSTOKEN_URL =
        "https://login.microsoftonline.com/9d54d085-32d3-43fc-aa24-e02bd9fc9c25/oauth2/v2.0/token"
    const val AZURE_APP_CONFIG_SECRETS_BASE_URL =
        "https://tawakal-dev-keyvault.vault.azure.net/secrets"
    const val AZURE_APP_CONFIG_SECRET_CLIENTID_KEY = "AzureB2CClientIdKotlin"
    const val AZURE_APP_CONFIG_CUSTOMERAPI_KEY = "TawakalPayKotlin:CustomerApiUrl"
    const val AZURE_APP_CONFIG_DEV_LABEL = "dev"
    const val AZURE_APP_CONFIG_GOOGLE_ADDRESS_API_KEY = "TawakalUkiOSGoogleApiKey"


    const val GET_METHOD = "GET"
    const val SECRET =
        "7y1ZxcyN9FZpoLReEETzJCaOt7vjJMmtUUy4LRaMGpGpdoWdWsTpJQQJ99AGACYeBjF9il3fAAABAZAC2o5c"
    const val CREDENTIAL = "y8Qi"
    const val SIGNED_HEADERS = "x-ms-date;host;x-ms-content-sha256"

    const val AZURE_APP_CONFIG_BASE_URL = "https://backendapiconfig.azconfig.io/kv"
    const val API_VERSION = "1.0"

    const val GOOGLE_ADDRESS_BASE_URL =
        "https://addressvalidation.googleapis.com/v1:validateAddress"

}