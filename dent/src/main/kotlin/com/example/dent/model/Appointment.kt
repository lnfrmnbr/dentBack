package com.example.dent.model

import java.time.LocalDateTime
import java.util.*

data class Appointment(
    val id: UUID? = null,
    val doctorId: UUID,
    val patientId: UUID,
    val patientFullName: String? = null,

    val date: LocalDateTime = LocalDateTime.now(),
    val tags: List<String>? = null,
    val complaints: String? = null,
    val comments: String? = null,
    val plan: String? = null,
    val isFirst: Boolean = true,
    val diagnosis: List<String>? = null,

    val chart: List<Tooth>? = null,
    val indexes: Indexes = Indexes()
)

data class Indexes(
    val bop: Double = 0.0,
    val api: Double = 0.0,
    val russel: Double = 0.0,
    val ohis: Double = 0.0,
    val kpu: Int = 0,
    val cpitn: List<Int> = listOf()
)

data class Tooth(
    val id: Int,
    val svgRes: Int,
    val isImplant: Boolean = false,
    val mobility: Int? = null,
    val oral: ToothParams = ToothParams(),
    val vestibular: ToothParams = ToothParams(),
    val removed: Boolean = false,
    val hasSeal: Boolean = false,
    val pulpPerio: Int = 0
)

data class ToothParams(
    val bleeding: BoolPoints = BoolPoints(),
    val stones: BoolPoints = BoolPoints(),
    val plaque: BoolPoints = BoolPoints(),
    val pus: BoolPoints = BoolPoints(),
    val furcation: Furcation = Furcation(),
    val caries: Caries = Caries(),
    val probingDepth: IntPoints = IntPoints(),
    val gingivalMargin: IntPoints = IntPoints()
)

data class Caries(
    val c11: Boolean = false,
    val c12: Boolean = false,
    val c13: Boolean = false,
    val c21: Boolean = false,
    val c22: Boolean = false,
    val c23: Boolean = false
) : java.io.Serializable

data class BoolPoints(
    val p1: Boolean = false,
    val p2: Boolean = false,
    val p3: Boolean = false
)

data class IntPoints(
    val p1: Int? = null,
    val p2: Int? = null,
    val p3: Int? = null
)

data class Furcation(
    val root1: Int = 0,
    val root2: Int = 0
)