package com.nacho.expense.domain

import javax.persistence.*
import com.nacho.person.domain.Person
import java.time.LocalDateTime
import java.math.BigDecimal

@Entity
@Table(name = "EXPENSE")
class Expense(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Column(name = "description")
    val description: String,

    @Column(name = "amount")
    val amount: BigDecimal,

    @Column(name = "date")
    val date: LocalDateTime,

    @ManyToOne
    @JoinColumn(name = "person_id")
    val payer: Person,

    @ManyToMany
    @JoinTable(
        name = "EXPENSE_DEODORS",
        joinColumns = [JoinColumn(name = "expense_id")],
        inverseJoinColumns = [JoinColumn(name = "person_id")]
    )
    val deodors: List<Person>
)
