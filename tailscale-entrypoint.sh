#!/bin/bash
set -e
tailscaled --state=/var/lib/tailscale/tailscaled.state &
sleep 5
tailscale up --authkey=${TAILSCALE_AUTHKEY} --accept-routes
tailscale funnel 8080
tail -f /dev/null
