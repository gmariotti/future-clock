package com.mariotti.developer.futureclock.extensions

import com.squareup.sqlbrite.BriteDatabase

inline fun BriteDatabase.performTransaction(dbOperations: () -> Unit) {
	val transaction = this.newTransaction()
	try {
		dbOperations()
		transaction.markSuccessful()
	} finally {
		transaction.end()
	}
}