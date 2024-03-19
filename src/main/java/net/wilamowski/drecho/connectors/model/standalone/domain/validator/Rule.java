package net.wilamowski.drecho.connectors.model.standalone.domain.validator;

interface Rule<T> {

  ValidationResult check(T value);
}
