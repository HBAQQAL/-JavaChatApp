package com.client.helpers;

import java.util.ArrayList;

import com.client.model.Contact;

public class ContactManager {
    private ArrayList<Contact> contacts;
    private int size;
    private Contact selectedContact;
    private static ContactManager instance = null;

    private ContactManager() {
        this.contacts = new ArrayList<Contact>();
        this.size = 0;
        this.selectedContact = null;
    }

    public static ContactManager getInstance() {
        if (instance == null) {
            instance = new ContactManager();
        }
        return instance;
    }

    public void addContact(Contact contact) {
        if (this.isAlreadyAdded(contact)) {
            return;
        }
        this.contacts.add(contact);
        this.size++;
    }

    public void removeContact(Contact contact) {
        if (!this.isAlreadyAdded(contact)) {
            return;
        }
        this.contacts.remove(contact);
        this.size--;
    }

    public ArrayList<Contact> getContacts() {
        return this.contacts;
    }

    public void soutContacts() {
        for (Contact c : this.contacts) {
            System.out.println(c.getNames());
        }
    }

    public void setSelectedContact(Contact contact) {

        this.selectedContact = contact;
        System.out.println("getting all messages");
        ChatManager.getInstance().getAllMessages();
    }

    public void setSelectedContact(String username) {
        for (Contact c : this.contacts) {
            if (c.getUsername().equals(username)) {
                this.selectedContact = c;
                break;
            }
        }

        ChatManager.getInstance().getAllMessages();
    }

    public Contact getSelectedContact() {
        return this.selectedContact;
    }

    public boolean hasSelectedContact() {
        return this.selectedContact != null;
    }

    private boolean isAlreadyAdded(Contact contact) {
        for (Contact c : this.contacts) {
            if (c.getUsername().equals(contact.getUsername())) {
                return true;
            }
        }
        return false;
    }

}
