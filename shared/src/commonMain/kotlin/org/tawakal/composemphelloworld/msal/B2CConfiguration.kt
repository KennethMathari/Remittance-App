package org.tawakal.composemphelloworld.msal

import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

object B2CConfiguration {
    /**
     * Name of the policies/user flows in your B2C tenant.
     * See https://docs.microsoft.com/en-us/azure/active-directory-b2c/active-directory-b2c-reference-policies for more info.
     */
    @JvmField
    val Policies = arrayOf(
        "B2C_1A_SIGNUP_SIGNIN", "B2C_1A_PROFILEEDIT"
    )

    /**
     * Name of your B2C tenant hostname.
     */
    const val azureAdB2CHostName = "8tawakal.b2clogin.com"

    /**
     * Name of your B2C tenant.
     */
    const val tenantName = "8tawakal.onmicrosoft.com"

    /**
     * Returns an authority for the given policy name.
     *
     * @param policyName name of a B2C policy.
     */
    @JvmStatic
    fun getAuthorityFromPolicyName(policyName: String): String {
        return "https://$azureAdB2CHostName/tfp/$tenantName/$policyName/"
    }

    @JvmStatic
    val scopes: List<String>
        /**
         * Returns an array of scopes you wish to acquire as part of the returned token result.
         * These scopes must be added in your B2C application page.
         */
        get() = mutableListOf(
            "607fea3b-46f4-4817-ab09-a7bd1a10ca1a"
        )
}