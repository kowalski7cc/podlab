# podLAB

Build a simple lab using podman and systemd, ready for exercising with Ansible

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

You can use option `-a` to attach to the new container.

Finally you can run your playbooks with:

```sh
ansible-playbook ./playbooks/example.yml
```
