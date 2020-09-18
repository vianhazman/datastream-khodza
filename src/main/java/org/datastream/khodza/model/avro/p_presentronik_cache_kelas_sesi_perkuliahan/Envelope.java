/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package org.datastream.khodza.model.avro.p_presentronik_cache_kelas_sesi_perkuliahan;

import org.apache.avro.specific.SpecificData;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class Envelope extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -5335207820411016213L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Envelope\",\"namespace\":\"org.datastream.khodza.model.avro.p_presentronik_cache_kelas_sesi_perkuliahan\",\"fields\":[{\"name\":\"before\",\"type\":[\"null\",{\"type\":\"record\",\"name\":\"Value\",\"fields\":[{\"name\":\"id_kuliah\",\"type\":\"long\"},{\"name\":\"kd_kelas\",\"type\":\"string\"},{\"name\":\"tanggal\",\"type\":{\"type\":\"int\",\"connect.version\":1,\"connect.name\":\"io.debezium.time.Date\"}},{\"name\":\"wkt_mulai\",\"type\":{\"type\":\"long\",\"connect.version\":1,\"connect.name\":\"io.debezium.time.MicroTimestamp\"}},{\"name\":\"wkt_selesai\",\"type\":{\"type\":\"long\",\"connect.version\":1,\"connect.name\":\"io.debezium.time.MicroTimestamp\"}},{\"name\":\"ts_update\",\"type\":{\"type\":\"long\",\"connect.version\":1,\"connect.name\":\"io.debezium.time.MicroTimestamp\"}}],\"connect.name\":\"p_presentronik.presentronik.cache_kelas_sesi_perkuliahan.Value\"}],\"default\":null},{\"name\":\"after\",\"type\":[\"null\",\"Value\"],\"default\":null},{\"name\":\"source\",\"type\":{\"type\":\"record\",\"name\":\"Source\",\"namespace\":\"io.debezium.connector.postgresql\",\"fields\":[{\"name\":\"version\",\"type\":\"string\"},{\"name\":\"connector\",\"type\":\"string\"},{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"ts_ms\",\"type\":\"long\"},{\"name\":\"snapshot\",\"type\":[{\"type\":\"string\",\"connect.version\":1,\"connect.parameters\":{\"allowed\":\"true,last,false\"},\"connect.default\":\"false\",\"connect.name\":\"io.debezium.data.Enum\"},\"null\"],\"default\":\"false\"},{\"name\":\"db\",\"type\":\"string\"},{\"name\":\"schema\",\"type\":\"string\"},{\"name\":\"table\",\"type\":\"string\"},{\"name\":\"txId\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"lsn\",\"type\":[\"null\",\"long\"],\"default\":null},{\"name\":\"xmin\",\"type\":[\"null\",\"long\"],\"default\":null}],\"connect.name\":\"io.debezium.connector.postgresql.Source\"}},{\"name\":\"op\",\"type\":\"string\"},{\"name\":\"ts_ms\",\"type\":[\"null\",\"long\"],\"default\":null}],\"connect.name\":\"p_presentronik.presentronik.cache_kelas_sesi_perkuliahan.Envelope\"}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  @Deprecated public Value before;
  @Deprecated public Value after;
  @Deprecated public io.debezium.connector.postgresql.Source source;
  @Deprecated public java.lang.CharSequence op;
  @Deprecated public java.lang.Long ts_ms;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Envelope() {}

  /**
   * All-args constructor.
   * @param before The new value for before
   * @param after The new value for after
   * @param source The new value for source
   * @param op The new value for op
   * @param ts_ms The new value for ts_ms
   */
  public Envelope(Value before, Value after, io.debezium.connector.postgresql.Source source, java.lang.CharSequence op, java.lang.Long ts_ms) {
    this.before = before;
    this.after = after;
    this.source = source;
    this.op = op;
    this.ts_ms = ts_ms;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return before;
    case 1: return after;
    case 2: return source;
    case 3: return op;
    case 4: return ts_ms;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: before = (Value)value$; break;
    case 1: after = (Value)value$; break;
    case 2: source = (io.debezium.connector.postgresql.Source)value$; break;
    case 3: op = (java.lang.CharSequence)value$; break;
    case 4: ts_ms = (java.lang.Long)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'before' field.
   * @return The value of the 'before' field.
   */
  public Value getBefore() {
    return before;
  }

  /**
   * Sets the value of the 'before' field.
   * @param value the value to set.
   */
  public void setBefore(Value value) {
    this.before = value;
  }

  /**
   * Gets the value of the 'after' field.
   * @return The value of the 'after' field.
   */
  public Value getAfter() {
    return after;
  }

  /**
   * Sets the value of the 'after' field.
   * @param value the value to set.
   */
  public void setAfter(Value value) {
    this.after = value;
  }

  /**
   * Gets the value of the 'source' field.
   * @return The value of the 'source' field.
   */
  public io.debezium.connector.postgresql.Source getSource() {
    return source;
  }

  /**
   * Sets the value of the 'source' field.
   * @param value the value to set.
   */
  public void setSource(io.debezium.connector.postgresql.Source value) {
    this.source = value;
  }

  /**
   * Gets the value of the 'op' field.
   * @return The value of the 'op' field.
   */
  public java.lang.CharSequence getOp() {
    return op;
  }

  /**
   * Sets the value of the 'op' field.
   * @param value the value to set.
   */
  public void setOp(java.lang.CharSequence value) {
    this.op = value;
  }

  /**
   * Gets the value of the 'ts_ms' field.
   * @return The value of the 'ts_ms' field.
   */
  public java.lang.Long getTsMs() {
    return ts_ms;
  }

  /**
   * Sets the value of the 'ts_ms' field.
   * @param value the value to set.
   */
  public void setTsMs(java.lang.Long value) {
    this.ts_ms = value;
  }

  /**
   * Creates a new Envelope RecordBuilder.
   * @return A new Envelope RecordBuilder
   */
  public static Envelope.Builder newBuilder() {
    return new Envelope.Builder();
  }

  /**
   * Creates a new Envelope RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Envelope RecordBuilder
   */
  public static Envelope.Builder newBuilder(Envelope.Builder other) {
    return new Envelope.Builder(other);
  }

  /**
   * Creates a new Envelope RecordBuilder by copying an existing Envelope instance.
   * @param other The existing instance to copy.
   * @return A new Envelope RecordBuilder
   */
  public static Envelope.Builder newBuilder(Envelope other) {
    return new Envelope.Builder(other);
  }

  /**
   * RecordBuilder for Envelope instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Envelope>
    implements org.apache.avro.data.RecordBuilder<Envelope> {

    private Value before;
    private Value.Builder beforeBuilder;
    private Value after;
    private Value.Builder afterBuilder;
    private io.debezium.connector.postgresql.Source source;
    private io.debezium.connector.postgresql.Source.Builder sourceBuilder;
    private java.lang.CharSequence op;
    private java.lang.Long ts_ms;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(Envelope.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.before)) {
        this.before = data().deepCopy(fields()[0].schema(), other.before);
        fieldSetFlags()[0] = true;
      }
      if (other.hasBeforeBuilder()) {
        this.beforeBuilder = Value.newBuilder(other.getBeforeBuilder());
      }
      if (isValidValue(fields()[1], other.after)) {
        this.after = data().deepCopy(fields()[1].schema(), other.after);
        fieldSetFlags()[1] = true;
      }
      if (other.hasAfterBuilder()) {
        this.afterBuilder = Value.newBuilder(other.getAfterBuilder());
      }
      if (isValidValue(fields()[2], other.source)) {
        this.source = data().deepCopy(fields()[2].schema(), other.source);
        fieldSetFlags()[2] = true;
      }
      if (other.hasSourceBuilder()) {
        this.sourceBuilder = io.debezium.connector.postgresql.Source.newBuilder(other.getSourceBuilder());
      }
      if (isValidValue(fields()[3], other.op)) {
        this.op = data().deepCopy(fields()[3].schema(), other.op);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.ts_ms)) {
        this.ts_ms = data().deepCopy(fields()[4].schema(), other.ts_ms);
        fieldSetFlags()[4] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing Envelope instance
     * @param other The existing instance to copy.
     */
    private Builder(Envelope other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.before)) {
        this.before = data().deepCopy(fields()[0].schema(), other.before);
        fieldSetFlags()[0] = true;
      }
      this.beforeBuilder = null;
      if (isValidValue(fields()[1], other.after)) {
        this.after = data().deepCopy(fields()[1].schema(), other.after);
        fieldSetFlags()[1] = true;
      }
      this.afterBuilder = null;
      if (isValidValue(fields()[2], other.source)) {
        this.source = data().deepCopy(fields()[2].schema(), other.source);
        fieldSetFlags()[2] = true;
      }
      this.sourceBuilder = null;
      if (isValidValue(fields()[3], other.op)) {
        this.op = data().deepCopy(fields()[3].schema(), other.op);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.ts_ms)) {
        this.ts_ms = data().deepCopy(fields()[4].schema(), other.ts_ms);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'before' field.
      * @return The value.
      */
    public Value getBefore() {
      return before;
    }

    /**
      * Sets the value of the 'before' field.
      * @param value The value of 'before'.
      * @return This builder.
      */
    public Envelope.Builder setBefore(Value value) {
      validate(fields()[0], value);
      this.beforeBuilder = null;
      this.before = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'before' field has been set.
      * @return True if the 'before' field has been set, false otherwise.
      */
    public boolean hasBefore() {
      return fieldSetFlags()[0];
    }

    /**
     * Gets the Builder instance for the 'before' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public Value.Builder getBeforeBuilder() {
      if (beforeBuilder == null) {
        if (hasBefore()) {
          setBeforeBuilder(Value.newBuilder(before));
        } else {
          setBeforeBuilder(Value.newBuilder());
        }
      }
      return beforeBuilder;
    }

    /**
     * Sets the Builder instance for the 'before' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public Envelope.Builder setBeforeBuilder(Value.Builder value) {
      clearBefore();
      beforeBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'before' field has an active Builder instance
     * @return True if the 'before' field has an active Builder instance
     */
    public boolean hasBeforeBuilder() {
      return beforeBuilder != null;
    }

    /**
      * Clears the value of the 'before' field.
      * @return This builder.
      */
    public Envelope.Builder clearBefore() {
      before = null;
      beforeBuilder = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'after' field.
      * @return The value.
      */
    public Value getAfter() {
      return after;
    }

    /**
      * Sets the value of the 'after' field.
      * @param value The value of 'after'.
      * @return This builder.
      */
    public Envelope.Builder setAfter(Value value) {
      validate(fields()[1], value);
      this.afterBuilder = null;
      this.after = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'after' field has been set.
      * @return True if the 'after' field has been set, false otherwise.
      */
    public boolean hasAfter() {
      return fieldSetFlags()[1];
    }

    /**
     * Gets the Builder instance for the 'after' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public Value.Builder getAfterBuilder() {
      if (afterBuilder == null) {
        if (hasAfter()) {
          setAfterBuilder(Value.newBuilder(after));
        } else {
          setAfterBuilder(Value.newBuilder());
        }
      }
      return afterBuilder;
    }

    /**
     * Sets the Builder instance for the 'after' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public Envelope.Builder setAfterBuilder(Value.Builder value) {
      clearAfter();
      afterBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'after' field has an active Builder instance
     * @return True if the 'after' field has an active Builder instance
     */
    public boolean hasAfterBuilder() {
      return afterBuilder != null;
    }

    /**
      * Clears the value of the 'after' field.
      * @return This builder.
      */
    public Envelope.Builder clearAfter() {
      after = null;
      afterBuilder = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'source' field.
      * @return The value.
      */
    public io.debezium.connector.postgresql.Source getSource() {
      return source;
    }

    /**
      * Sets the value of the 'source' field.
      * @param value The value of 'source'.
      * @return This builder.
      */
    public Envelope.Builder setSource(io.debezium.connector.postgresql.Source value) {
      validate(fields()[2], value);
      this.sourceBuilder = null;
      this.source = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'source' field has been set.
      * @return True if the 'source' field has been set, false otherwise.
      */
    public boolean hasSource() {
      return fieldSetFlags()[2];
    }

    /**
     * Gets the Builder instance for the 'source' field and creates one if it doesn't exist yet.
     * @return This builder.
     */
    public io.debezium.connector.postgresql.Source.Builder getSourceBuilder() {
      if (sourceBuilder == null) {
        if (hasSource()) {
          setSourceBuilder(io.debezium.connector.postgresql.Source.newBuilder(source));
        } else {
          setSourceBuilder(io.debezium.connector.postgresql.Source.newBuilder());
        }
      }
      return sourceBuilder;
    }

    /**
     * Sets the Builder instance for the 'source' field
     * @param value The builder instance that must be set.
     * @return This builder.
     */
    public Envelope.Builder setSourceBuilder(io.debezium.connector.postgresql.Source.Builder value) {
      clearSource();
      sourceBuilder = value;
      return this;
    }

    /**
     * Checks whether the 'source' field has an active Builder instance
     * @return True if the 'source' field has an active Builder instance
     */
    public boolean hasSourceBuilder() {
      return sourceBuilder != null;
    }

    /**
      * Clears the value of the 'source' field.
      * @return This builder.
      */
    public Envelope.Builder clearSource() {
      source = null;
      sourceBuilder = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'op' field.
      * @return The value.
      */
    public java.lang.CharSequence getOp() {
      return op;
    }

    /**
      * Sets the value of the 'op' field.
      * @param value The value of 'op'.
      * @return This builder.
      */
    public Envelope.Builder setOp(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.op = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'op' field has been set.
      * @return True if the 'op' field has been set, false otherwise.
      */
    public boolean hasOp() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'op' field.
      * @return This builder.
      */
    public Envelope.Builder clearOp() {
      op = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'ts_ms' field.
      * @return The value.
      */
    public java.lang.Long getTsMs() {
      return ts_ms;
    }

    /**
      * Sets the value of the 'ts_ms' field.
      * @param value The value of 'ts_ms'.
      * @return This builder.
      */
    public Envelope.Builder setTsMs(java.lang.Long value) {
      validate(fields()[4], value);
      this.ts_ms = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'ts_ms' field has been set.
      * @return True if the 'ts_ms' field has been set, false otherwise.
      */
    public boolean hasTsMs() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'ts_ms' field.
      * @return This builder.
      */
    public Envelope.Builder clearTsMs() {
      ts_ms = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    public Envelope build() {
      try {
        Envelope record = new Envelope();
        if (beforeBuilder != null) {
          record.before = this.beforeBuilder.build();
        } else {
          record.before = fieldSetFlags()[0] ? this.before : (Value) defaultValue(fields()[0]);
        }
        if (afterBuilder != null) {
          record.after = this.afterBuilder.build();
        } else {
          record.after = fieldSetFlags()[1] ? this.after : (Value) defaultValue(fields()[1]);
        }
        if (sourceBuilder != null) {
          record.source = this.sourceBuilder.build();
        } else {
          record.source = fieldSetFlags()[2] ? this.source : (io.debezium.connector.postgresql.Source) defaultValue(fields()[2]);
        }
        record.op = fieldSetFlags()[3] ? this.op : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.ts_ms = fieldSetFlags()[4] ? this.ts_ms : (java.lang.Long) defaultValue(fields()[4]);
        return record;
      } catch (Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  private static final org.apache.avro.io.DatumWriter
    WRITER$ = new org.apache.avro.specific.SpecificDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  private static final org.apache.avro.io.DatumReader
    READER$ = new org.apache.avro.specific.SpecificDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}
