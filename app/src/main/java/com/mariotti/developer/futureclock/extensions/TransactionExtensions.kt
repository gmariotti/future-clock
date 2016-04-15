package com.mariotti.developer.futureclock.extensions

import com.squareup.sqlbrite.BriteDatabase

inline fun <T> BriteDatabase.performTransaction(body: () -> T) {
	val transaction = this.newTransaction()
	try {
		body()
		transaction.markSuccessful()
	} finally {
		transaction.end()
	}
}