package net.wilamowski.drecho.standalone.domain.validator;

interface Rule<T> {

  ValidationResult check(T value);
}
