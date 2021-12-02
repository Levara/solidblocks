/*
 * This file is generated by jOOQ.
 */
package de.solidblocks.config.db.tables.records

import de.solidblocks.config.db.tables.Clouds
import org.jooq.Field
import org.jooq.Record1
import org.jooq.Record3
import org.jooq.Row3
import org.jooq.impl.UpdatableRecordImpl
import java.util.UUID

/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class CloudsRecord() : UpdatableRecordImpl<CloudsRecord>(Clouds.CLOUDS), Record3<UUID?, String?, Boolean?> {

    var id: UUID?
        set(value) = set(0, value)
        get() = get(0) as UUID?

    var name: String?
        set(value) = set(1, value)
        get() = get(1) as String?

    var deleted: Boolean?
        set(value) = set(2, value)
        get() = get(2) as Boolean?

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    override fun key(): Record1<UUID?> = super.key() as Record1<UUID?>

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    override fun fieldsRow(): Row3<UUID?, String?, Boolean?> = super.fieldsRow() as Row3<UUID?, String?, Boolean?>
    override fun valuesRow(): Row3<UUID?, String?, Boolean?> = super.valuesRow() as Row3<UUID?, String?, Boolean?>
    override fun field1(): Field<UUID?> = Clouds.CLOUDS.ID
    override fun field2(): Field<String?> = Clouds.CLOUDS.NAME
    override fun field3(): Field<Boolean?> = Clouds.CLOUDS.DELETED
    override fun component1(): UUID? = id
    override fun component2(): String? = name
    override fun component3(): Boolean? = deleted
    override fun value1(): UUID? = id
    override fun value2(): String? = name
    override fun value3(): Boolean? = deleted

    override fun value1(value: UUID?): CloudsRecord {
        this.id = value
        return this
    }

    override fun value2(value: String?): CloudsRecord {
        this.name = value
        return this
    }

    override fun value3(value: Boolean?): CloudsRecord {
        this.deleted = value
        return this
    }

    override fun values(value1: UUID?, value2: String?, value3: Boolean?): CloudsRecord {
        this.value1(value1)
        this.value2(value2)
        this.value3(value3)
        return this
    }

    /**
     * Create a detached, initialised CloudsRecord
     */
    constructor(id: UUID? = null, name: String? = null, deleted: Boolean? = null) : this() {
        this.id = id
        this.name = name
        this.deleted = deleted
    }
}
