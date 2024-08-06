package tech.uzpro.todoapp.model.payload;

public class ResponseEnum {
    // Auth
    public static final String USER_NAME_IS_INVALID = "Username is invalid";
    public static final String EMAIL_IS_INVALID = "Email is invalid";
    public static final String USER_NAME_ALREADY_TAKEN = "Username is already taken";
    public static final String INVALID_VERIFICATION_CODE = "Invalid verification code";
    public static final String VERIFICATION_CODE_SENT = "Verification code sent";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ACCOUNT_VERIFIED = "Account verified";
    public static final String USER_LOGGED_IN_SUCCESSFULLY = "User logged in successfully";
    public static final String INVALID_CREDENTIALS = "Invalid credentials";
    public static final String USER_NOT_VERIFICATION = "User not verification";
    public static final String INVALID_PASSWORD = "Invalid password";
    public static final String EMAIL_NOT_FOUND = "Email not found";
    public static final String USER_NAME_NOT_FOUND = "Username not found";
    public static final String EMAIL_OR_USER_NAME_IS_EMPTY = "Email or Username is empty";
    public static final String EMAIL_SENT = "Email sent";
    public static final String PASSWORD_RESET_SUCCESSFULLY = "Password reset successfully";
    // USER
    public static final String USER_FOUND = "User found";
    public static final String EMAIL_UPDATE = "Email updated";
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String PASSWORD_UPDATE = "Password updated";
    public static final String USER_DELETED = "User deleted";
    public static final String TASK_FOUND = "Task found";

    // TASK
    public static final String TASK_CREATED = "Task created";
    public static final String TASK_UPDATED = "Task updated";
    public static final String TASK_DELETED = "Task deleted";
    public static final String TASK_NOT_FOUND = "Task not found";
    public static final String NO_TASK_FOUND = "No task found";

}
