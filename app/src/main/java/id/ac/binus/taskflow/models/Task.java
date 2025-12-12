package id.ac.binus.taskflow.models;

import com.google.firebase.Timestamp;

public class Task {
    private String id;
    private String title;
    private String description;
    private String category;
    private String categoryId;
    private String priority; // Low, Medium, High
    private String status; // Pending, In Progress, Completed
    private Timestamp dueDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Task() {}

    public Task(String id, String title, String description, String category, String categoryId,
                String priority, String status, Timestamp dueDate, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.categoryId = categoryId;
        this.priority = priority;
        this.status = status;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
