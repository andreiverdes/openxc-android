package com.openxc.messages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import android.os.Parcel;

import static com.google.common.base.MoreObjects.toStringHelper;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

public class ModemCommand extends KeyedMessage {
    public static final String COMMAND_KEY = "modem_command";
    public static final String DIAGNOSTIC_REQUEST_KEY = "request";
    public static final String ACTION_KEY = "action";

    public enum CommandType {
        VERSION, DEVICE_ID, DIAGNOSTIC_REQUEST, DIAGNOSTICS_ENABLE, DIAGNOSTICS_DISABLE
    };

    public static final String[] sRequiredFieldsValues = new String[] {
            COMMAND_KEY };
    public static final Set<String> sRequiredFields = new HashSet<String>(
            Arrays.asList(sRequiredFieldsValues));

    @SerializedName(COMMAND_KEY)
    private CommandType mCommand;

    @SerializedName(ACTION_KEY)
    private String mAction;

    @SerializedName(DIAGNOSTIC_REQUEST_KEY)
    private DiagnosticRequest mDiagnosticRequest;

    public ModemCommand(CommandType command, String action) {
        mCommand = command;
        mAction = action;
    }

    public ModemCommand(CommandType command) {
        this(command, null);
    }

    public ModemCommand(DiagnosticRequest request, String action) {
        this(CommandType.DIAGNOSTIC_REQUEST, action);
        mDiagnosticRequest = request;
    }

    public CommandType getCommand() {
        return mCommand;
    }

    public boolean hasAction() {
        return mAction != null && !mAction.isEmpty();
    }

    public String getAction() {
        return mAction;
    }

    public DiagnosticRequest getDiagnosticRequest() {
        return mDiagnosticRequest;
    }

    @Override
    public MessageKey getKey() {
        if(super.getKey() == null) {
            HashMap<String, Object> key = new HashMap<>();
            key.put(COMMAND_KEY, getCommand());
            setKey(new MessageKey(key));
        }
        return super.getKey();
    }

    public static boolean containsRequiredFields(Set<String> fields) {
        return fields.containsAll(sRequiredFields);
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj) || !(obj instanceof Command)) {
            return false;
        }

        final Command other = (Command) obj;
        return Objects.equal(getCommand(), other.getCommand()) &&
                Objects.equal(getDiagnosticRequest(),
                        other.getDiagnosticRequest()) &&
                Objects.equal(getAction(), other.getAction());
    }

    @Override
    public String toString() {
        return toStringHelper(this)
            .add("timestamp", getTimestamp())
            .add("command", getCommand())
            .add("action", getAction())
            .add("diagnostic_request", getDiagnosticRequest())
            .add("extras", getExtras())
            .toString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeSerializable(getCommand());
        out.writeString(getAction());
        out.writeParcelable(getDiagnosticRequest(), flags);
    }

    @Override
    protected void readFromParcel(Parcel in) {
        super.readFromParcel(in);
        mCommand = (CommandType) in.readSerializable();
        mAction = in.readString();
        mDiagnosticRequest = (DiagnosticRequest) in.readParcelable(
                DiagnosticRequest.class.getClassLoader());
    }

    protected ModemCommand(Parcel in)
            throws UnrecognizedMessageTypeException {
        readFromParcel(in);
    }

    protected ModemCommand() { }

}