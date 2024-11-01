package org.tawakal.composemphelloworld.utils

import org.tawakal.composemphelloworld.data.model.serviceData.City
import org.tawakal.composemphelloworld.data.model.serviceData.Country
import org.tawakal.composemphelloworld.data.model.serviceData.IdentityDocument
import org.tawakal.composemphelloworld.data.model.serviceData.Purpose
import org.tawakal.composemphelloworld.data.model.serviceData.Relationship
import org.tawakal.composemphelloworld.data.model.serviceData.Service
import org.tawakal.composemphelloworld.data.model.serviceData.ServiceResponse
import org.tawakal.composemphelloworld.data.model.serviceData.SourceOfIncome
import org.tawakal.composemphelloworld.data.model.serviceData.SubService

object ServiceData {

    private const val STRING_REQUIRED = "String Required"
    private const val DATE = "Date (yyyy-MM-dd)"

    private val cashPickUpService = Service(
        title = "Cash pickup", content = "Pick up cash at one of many agents", subService = listOf(
            SubService(
                serviceName = "Cash", serviceCode = "00001"
            )
        )
    )

    private val mobileWalletService = Service(
        title = "Mobile Wallet", content = "Send to Sombank mobile wallet", subService = listOf(
            SubService(
                serviceName = "T-Plus", serviceCode = "00003"
            )
        )
    )

    private val uremitService = Service(
        title = "Uremit", content = "Uremit", subService = listOf(
            SubService(
                serviceName = "Cash", serviceCode = "00007"
            )
        )
    )

    private val mobileMoneyService = Service(
        title = "Mobile Money",
        content = "To a mobile wallet on your receipient's phone",
        subService = listOf(
            SubService(
                serviceName = "M-Pesa", serviceCode = "00014"
            )
        )
    )

    // Helper function to create IdentityDocument
    private fun createIdentityDocument(identityCode: String, identityDescription: String) = IdentityDocument(
        identityCode = identityCode,
        identityDescription = identityDescription,
        documentNumber = STRING_REQUIRED,
        countryRegion = STRING_REQUIRED,
        dateOfIssue = DATE,
        dateOfExpiration = DATE,
        dateOfBirth = DATE
    )

    // Define the documents using the helper function
    private val dlcDocument = createIdentityDocument("DLC", "DRIVING LICENSE")
    private val eidDocument = createIdentityDocument("EID", "EMIRATES ID")
    private val nationalIdDocument = createIdentityDocument("NID", "NATIONAL ID")
    private val passportDocument = createIdentityDocument("PPT", "PASSPORT")


    val serviceData = ServiceResponse(
        timeStamp = "03:22:52.2508525", statusCode = "200", status = "OK", countries = listOf(
            Country(
                countryCode = "AE", countryName = "UNITED ARAB EMIRATES", city = listOf(
                    City(
                        cityCode = "AEB0000005", cityName = "ABU DHABI"
                    ), City(
                        cityCode = "AEB0000004", cityName = "AJMAN"
                    ), City(
                        cityCode = "AEB0000007", cityName = "DUBAI"
                    ), City(
                        cityCode = "AEB0000010", cityName = "MUSAFAH"
                    ), City(
                        cityCode = "AEB0000002", cityName = "RASULKHAIMA"
                    )
                ), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, eidDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "BD",
                countryName = "BANGLADESH",
                city = emptyList(),
                service = listOf(
                    mobileWalletService, uremitService
                ),
                identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "CA", countryName = "CANADA", city = listOf(
                    City(
                        cityCode = "CAC0000010", cityName = "OTTAWA"
                    ), City(
                        cityCode = "CAC0000001", cityName = "TORONTO"
                    )
                ), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "CN", countryName = "CHINA", city = listOf(
                    City(
                        cityCode = "CNA0000001", cityName = "YIWU"
                    )
                ), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "DJ", countryName = "DJIBOUTI", city = listOf(
                    City(
                        cityCode = "DJA0000001", cityName = "DJIBOUTI"
                    )
                ), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "DK", countryName = "DENMARK", city = listOf(
                    City(
                        cityCode = "DKD0000003", cityName = "AALBORG"
                    ), City(
                        cityCode = "DKD0000006", cityName = "AALBORG CENTER"
                    ), City(
                        cityCode = "DKD0000008", cityName = "AARHUS"
                    ), City(
                        cityCode = "DKD0000001", cityName = "COPENHAGEN"
                    ), City(
                        cityCode = "DKD0000004", cityName = "ODENSE"
                    )
                ), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "EG", countryName = "EGYPT", city = listOf(
                    City(
                        cityCode = "EGA0000001", cityName = "CAIRO"
                    )
                ), service = listOf(
                    cashPickUpService, mobileWalletService, uremitService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "ER", countryName = "ERITREA", city = emptyList(), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "ET", countryName = "ETHIOPIA", city = listOf(
                    City(
                        cityCode = "ETH0000001", cityName = "WAJAALE ETHIOPIA"
                    ), City(
                        cityCode = "ETD0000001", cityName = "ADDISABABA"
                    ), City(
                        cityCode = "ETD0000039", cityName = "JIGJIGA TOWN"
                    ), City(
                        cityCode = "ETD0000012", cityName = "WAJALE ETHIOPIA"
                    )
                ), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "FR", countryName = "FRANCE", city = listOf(
                    City(
                        cityCode = "FRA0000002", cityName = "KODADKA AN SHAQEYN  NO WORK OFFICE"
                    )
                ), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "GB", countryName = "UK", city = listOf(
                    City(
                        cityCode = "GBA0000003", cityName = "LONDON"
                    )
                ), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "HO", countryName = "HOLAND", city = emptyList(), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "IN", countryName = "INDIA", city = listOf(
                    City(
                        cityCode = "INB0000001", cityName = "HYDERABAD"
                    ), City(
                        cityCode = "INB0000003", cityName = "NEW DELHI"
                    )
                ), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "KE", countryName = "KENYA", city = listOf(
                    City(
                        cityCode = "KEC0000022", cityName = "GARISSA"
                    ), City(
                        cityCode = "KEC0000012", cityName = "KENYA ALL"
                    ), City(
                        cityCode = "KEC0000029", cityName = "MANDERA"
                    ), City(
                        cityCode = "KEC0000017", cityName = "MOMBASA"
                    ), City(
                        cityCode = "KEC0000003", cityName = "NAIROBI"
                    )
                ), service = listOf(
                    cashPickUpService, mobileWalletService, mobileMoneyService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "MY", countryName = "MALAYSIA", city = emptyList(), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "OM", countryName = "OMAN", city = listOf(
                    City(
                        cityCode = "OMA0000001", cityName = "SALALA"
                    )
                ), service = listOf(
                    cashPickUpService, mobileWalletService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "PH",
                countryName = "PHILIPPINES",
                city = emptyList(),
                service = listOf(
                    mobileWalletService, uremitService
                ),
                identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "PK", countryName = "PAKISTAN", city = emptyList(), service = listOf(
                    mobileWalletService, uremitService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "SO", countryName = "SOMALIA", city = listOf(
                    City(
                        cityCode = "SOG0000024", cityName = "BADHAN"
                    ), City(
                        cityCode = "SOA0000005", cityName = "BARDHERE"
                    ), City(
                        cityCode = "SOL0000070", cityName = "BAYDHABO"
                    ), City(
                        cityCode = "SOA0000002", cityName = "BELED HAWO"
                    ), City(
                        cityCode = "SOL0000004", cityName = "BELEDWEYNE"
                    ), City(
                        cityCode = "SOH0000011", cityName = "BERBERA"
                    ), City(
                        cityCode = "SOH0000010", cityName = "BORAMA"
                    ), City(
                        cityCode = "SOS0000003", cityName = "BOSASO"
                    ), City(
                        cityCode = "SOH0000012", cityName = "BURCO CITY CENTER"
                    ), City(
                        cityCode = "SOG0000009", cityName = "BURTINLE"
                    ), City(
                        cityCode = "SOG0000012", cityName = "CABUDWAAQ"
                    ), City(
                        cityCode = "SOL0000052", cityName = "CADAADO"
                    ), City(
                        cityCode = "SOG0000025", cityName = "DANGOROYO"
                    ), City(
                        cityCode = "SOG0000003", cityName = "DHABAD"
                    ), City(
                        cityCode = "SOL0000005", cityName = "DHUSAMAREB"
                    ), City(
                        cityCode = "SOA0000003", cityName = "DOOLOW"
                    ), City(
                        cityCode = "SOS0000010", cityName = "GALDOGOB"
                    ), City(
                        cityCode = "SOS0000009", cityName = "GALKAYO"
                    ), City(
                        cityCode = "SOA0000006", cityName = "GARBAHAAREY"
                    ), City(
                        cityCode = "SOS0000004", cityName = "GAROWE"
                    ), City(
                        cityCode = "SOH0000001", cityName = "HARGEISA HQ"
                    ), City(
                        cityCode = "SOL0000068", cityName = "JAWHAR"
                    ), City(
                        cityCode = "SOI0000002", cityName = "KISMAYO"
                    ), City(
                        cityCode = "SOA0000004", cityName = "LUUQ"
                    ), City(
                        cityCode = "SOL0000057", cityName = "MARKA"
                    ), City(
                        cityCode = "SOS0000007", cityName = "MOGADISHO HAWLWADAAG-SOMBANK"
                    ), City(
                        cityCode = "SOL0000001", cityName = "MOGADISHU HQ"
                    ), City(
                        cityCode = "SOS0000014", cityName = "QARDHO"
                    ), City(
                        cityCode = "SON0000001", cityName = "WAJAALE HQ"
                    )
                ), service = listOf(
                    mobileWalletService, cashPickUpService, Service(
                        title = "Mobile Money",
                        content = "To a mobile wallet on your receipient's phone",
                        subService = listOf(
                            SubService(
                                serviceName = "Hormuud", serviceCode = "00006"
                            ), SubService(
                                serviceName = "Golis", serviceCode = "00006"
                            ), SubService(
                                serviceName = "Telesom", serviceCode = "00006"
                            )
                        )
                    ), Service(
                        title = "Bank deposit",
                        content = "Send money to Bank account",
                        subService = listOf(
                            SubService(
                                serviceName = "SOMBANK", serviceCode = "00010"
                            )
                        )
                    )
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument, IdentityDocument(
                        identityCode = "TCL",
                        identityDescription = "TRADE LICENSE",
                        documentNumber = STRING_REQUIRED,
                        countryRegion = STRING_REQUIRED,
                        dateOfIssue = DATE,
                        dateOfExpiration = DATE,
                        dateOfBirth = DATE
                    )
                )
            ), Country(
                countryCode = "TR", countryName = "TURKEY", city = listOf(
                    City(
                        cityCode = "TRC0000002", cityName = "ANKARA"
                    ), City(
                        cityCode = "TRC0000001", cityName = "ISTANBUL"
                    )
                ), service = listOf(
                    mobileWalletService, cashPickUpService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "UG", countryName = "UGANDA", city = listOf(
                    City(
                        cityCode = "UGB0000001", cityName = "KAMPALA"
                    )
                ), service = listOf(
                    mobileWalletService, cashPickUpService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "US", countryName = "USA", city = listOf(
                    City(
                        cityCode = "USH0000204", cityName = "ATLANTA"
                    ), City(
                        cityCode = "USH0000217", cityName = "COLUMBUS"
                    ), City(
                        cityCode = "USH0000001", cityName = "MINNEAPOLIS"
                    ), City(
                        cityCode = "USH0000083", cityName = "ST PAUL"
                    )
                ), service = listOf(
                    mobileWalletService, cashPickUpService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            ), Country(
                countryCode = "ZA", countryName = "SOUTH AFRICA", city = listOf(
                    City(
                        cityCode = "ZAB0000001", cityName = "CAPETOWN"
                    ), City(
                        cityCode = "ZAF0000006", cityName = "JOHANESBURG"
                    ), City(
                        cityCode = "ZAF0000001", cityName = "PORT ELIZAPETH"
                    ), City(
                        cityCode = "ZAF0000010", cityName = "PRETORIA"
                    )
                ), service = listOf(
                    mobileWalletService, cashPickUpService
                ), identityDocument = listOf(
                    dlcDocument, nationalIdDocument, passportDocument
                )
            )
        ), purpose = listOf(
            Purpose(
                purposeCode = "001", purposeDescription = "PERSONAL USE"
            ), Purpose(
                purposeCode = "002", purposeDescription = "SALARY"
            ), Purpose(
                purposeCode = "003", purposeDescription = "FAMILY SUPPORT"
            ), Purpose(
                purposeCode = "009", purposeDescription = "CHARITY"
            ), Purpose(
                purposeCode = "011", purposeDescription = "OTHER"
            )
        ), relationship = listOf(
            Relationship(
                relationshipCode = "001",
                relationshipTitle = "Parent",
                relationshipDescription = "Mother,father,grandparent"
            ), Relationship(
                relationshipCode = "002",
                relationshipTitle = "Spouse",
                relationshipDescription = "Wife, husband, partner"
            ), Relationship(
                relationshipCode = "003",
                relationshipTitle = "Sibling",
                relationshipDescription = "Sister, brother"
            ), Relationship(
                relationshipCode = "004",
                relationshipTitle = "Child",
                relationshipDescription = "Daughter, son, grandchild"
            ), Relationship(
                relationshipCode = "005",
                relationshipTitle = "Other family member",
                relationshipDescription = "Aunt, uncle, cousin, friend"
            )
        ), sourceOfIncome = listOf(
            SourceOfIncome(
                sourceCode = "007", sourceDescription = "CHARITY"
            ), SourceOfIncome(
                sourceCode = "100", sourceDescription = "WORK"
            ), SourceOfIncome(
                sourceCode = "102", sourceDescription = "PROPERTY SOLD"
            ), SourceOfIncome(
                sourceCode = "104", sourceDescription = "PERSONAL INCOME"
            ), SourceOfIncome(
                sourceCode = "105", sourceDescription = "BUSINESS"
            ), SourceOfIncome(
                sourceCode = "445", sourceDescription = "OTHER"
            )
        )
    )
}