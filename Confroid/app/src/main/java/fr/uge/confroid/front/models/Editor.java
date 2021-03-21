package fr.uge.confroid.front.models;

public interface Editor {
    void save();
    void push(EditorSession session);
    EditorSession pop();
}
