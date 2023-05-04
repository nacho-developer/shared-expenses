package com.nacho.person.domain

import javax.persistence.*
import com.nacho.expense.domain.Expense

@Entity
@Table(name = "PERSON")
class Person(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    @Column(name = "name")
    val name: String,

    @OneToMany(mappedBy = "payer", fetch = FetchType.EAGER)
    val expenses: MutableList<Expense> = mutableListOf()
)
