# Ansible Lab

Build a simple lab ready for exercising with Ansible

## Requirements

- systemd
- podman
- buildah
- python
- ssh-keygen, ssh
- ansible

## Setup

First run `./setup`, then start pods with

```sh
./boot [group]
```

and finally you can run your playbooks like

```sh
ansible-playbook ./playbooks/example.yml
```
