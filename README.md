# IdentityTheft
![paper](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/paper_vector.svg) ![purpur](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/purpur_vector.svg) ![spigot](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/unsupported/spigot_vector.svg) <a href="https://modrinth.com/plugin/identitytheft/">
<img alt="modrinth" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg">
</a>



This plugin allows you to join as other players. Unlike other plugins, rather than simply disguising your skin and username, this plugin actually modifies what player you join as, allowing you take their inventory, convince other plugins, and play the server as them!

Sending messages in chat will **not** work unless if `enforce-secure-profile` is disabled in `server.properties`.

> ⚠ **Important Note** ⚠
>
> Since this plugin allows you to join as other players, make sure that you don't give normal players permission to use commands from this plugin, as they could simply join as an admin.

## Commands

### `/it become <player>`

Makes you become a player. Accepts either a username or a UUID.

**Note**: You will also take on that player's permissions! Make sure to allow regular players to run `/it reset` or have console access to ensure that you can return to normal.

### `/it set <player> <other>`

Makes someone else play as another player. Be wise, and don't turn other players into admins.

### `/it reset [player]`

Without a player specified, makes you stop taking the identity of another player.

When a player is specified, makes another player stop having a fake identity.

**Note**: Provide the real player's username, not who they're pretending to be.

### `/it reload`

Reloads the plugin configuration.

## Permissions

### `identitytheft.command.identitytheft`
Default: `true`

Allows players to access the root `/it` command.

While giving all players access to this command may seem counterintuitive, it ensures that admins will have access to `/it reset` even while being other players. You can revoke this permission if you have console access and will reset your identity from there.

### `identitytheft.command.identitytheft.become`
Default: `false`

Allows using `/it become <player>` to change identity.

Be very careful when giving out this permission!

### `identitytheft.command.identitytheft.set`
Default: `false`

Allows using `/it become <player> <other>` to change the identity of others.

Be careful when giving out this permission as well.

### `identitytheft.command.identitytheft.reset`
Default: `true`

Allows using `/it reset` to reset your own identity.

### `identitytheft.command.identitytheft.reset.others`
Default: `false`

Allows using `/it reset` to reset the identity of others.

### `identitytheft.command.identitytheft.reload`
Default: `false`

Allows using `/it reload`.

## Configuration

### `op-protection`

Default: `true`

Prevents players from becoming players with op, unless if they are op. This is only a last-resort safeguard, and you should not depend on this, as they could still join as other high-permission players. Don't give regular players access, even with this on!